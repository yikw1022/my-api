package open.api.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.protocol.x.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.constant.RedisConstant;
import open.api.exception.SystemException;
import open.api.mapper.*;
import open.api.model.dto.RegisterUserDto;
import open.api.model.dto.SearchUserDto;
import open.api.model.dto.UserDto;
import open.api.model.dto.UserEditDto;
import open.api.model.entity.*;
import open.api.model.enums.MenuTypeEnum;
import open.api.model.enums.UserStatusEnum;
import open.api.model.vo.UserVo;
import open.api.response.ResponseCodeEnum;
import open.api.service.IUserService;
import open.api.utils.JwtUtil;
import open.api.utils.RandomNumberUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 邮件发送者
     */
    @Value("${spring.mail.username}")
    private String username;

    private final UserRoleMapper userRoleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final MenuMapper menuMapper;
    private final RoleMapper roleMapper;
    private final RedisTemplate redisTemplate;
    /**
     * 注入QQ发送邮件的bean
     */
    @Autowired
    private JavaMailSender javaMailSender;

    private static final String MACHINE_TITLE = "API开放平台邮箱注册";

    private static final String TEMPLATE = "尊敬的用户，您正在注册API开放平台，验证码为";

    @Override
    public String login(User user) throws SystemException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        User selectOne = baseMapper.selectOne(wrapper);
        if(selectOne == null){
            throw new SystemException(ResponseCodeEnum.USER_NOT_EXITS);
        }
        //对比用户密码是否正确
        if(!selectOne.getPassword().equalsIgnoreCase(SecureUtil.md5(user.getPassword()))){
            throw new SystemException(ResponseCodeEnum.PASSWOR_ERROR);
        }
        //查看用户是否禁用
        if(selectOne.getStatus().intValue() == UserStatusEnum.FORBIDDEN.getCode()){
            throw new SystemException(ResponseCodeEnum.USER_FORBIDDEN);
        }
        //获取当前用户所拥有的所有菜单id
        log.info("用户{}正在登录.....",selectOne.getUsername());
        String token = JwtUtil.createToken(selectOne.getId());
        log.info("id为{}的用户的token值为{}",selectOne.getId(),token);
        redisTemplate.opsForValue().set(RedisConstant.TOKEN_CACHE_KEY + selectOne.getId(),token,RedisConstant.TOKEN_CACHE_EXPIRED, TimeUnit.MINUTES);
        log.info("用户{}登录成功！",selectOne.getUsername());
        return token;
    }

    @Override
    public Map<String, Object> getUserInfo(Integer userId) throws SystemException {
        User user = baseMapper.selectById(userId);
        if(user == null){
            throw new SystemException(ResponseCodeEnum.USER_NOT_EXITS);
        }
        //获取当前用户所拥有的所有菜单id
        log.info("用户{}正在菜单和按钮数据.....",user.getUsername());
        //判断当前是否拥有管理员权限
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Integer> roleIds = userRoleMapper.selectList(wrapper).stream().map(UserRole::getRoleId).collect(Collectors.toList());
        boolean isAdmin = false;
        if(!CollectionUtils.isEmpty(roleIds)){
            isAdmin = this.hasAdminPermission(roleIds);
        }
        if((user.getUsername().equalsIgnoreCase("admin")) || (isAdmin)){
            //如果用户名是admin则查询所有的菜单和按钮
            return this.queryAdminInfo(user);
        }else{
            //其他角色的用户查询相应的菜单和按钮
            return this.queryOtherInfo(user);
        }
    }

    private boolean hasAdminPermission(List<Integer> roleIds) {
        boolean isAdmin = false;
        List<Role> roleList = roleMapper.selectBatchIds(roleIds);
        for (int i = 0; i < roleList.size(); i++) {
            if(roleList.get(i).getRoleName().equalsIgnoreCase("管理员")){
                isAdmin = true;
                break;
            }
        }
        return isAdmin;
    }

    private Map<String, Object> queryOtherInfo(User user) {
        //查询相应的目录以及菜单
        //查询用户的角色ids
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",user.getId());
        List<Integer> roleIds = userRoleMapper.selectList(wrapper).stream().map(UserRole::getRoleId).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(roleIds)){
            //说明此用户已经被管理员分配过角色
            //根据角色ids查询相应的所有目录、菜单、按钮，追后过滤出routes和buttons即可
            //存储所有的菜单ids，使用Set存储的目的是去重相同的menuId，因为不同的角色可能会分配到相同的菜单或者按钮权限
            Set<Integer> menuIdSet = new HashSet<>();
            roleIds.stream().forEach(roleId -> {
                QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("role_id",roleId);
                List<Integer> menuIds = roleMenuMapper.selectList(queryWrapper).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
                menuIdSet.addAll(menuIds);
            });
            if(!CollectionUtils.isEmpty(menuIdSet)){
                List<Menu> menuList = menuMapper.selectBatchIds(menuIdSet);
                //过滤出routes和button
                List<String> routes = menuList.stream()
                        .filter(menu -> (menu.getType() == MenuTypeEnum.DIRECTORY.getCode()) || (menu.getType() == MenuTypeEnum.MENU.getCode()))
                        .collect(Collectors.toList())
                        .stream().map(Menu::getName).collect(Collectors.toList());
                List<String> buttons = menuList.stream()
                        .filter(menu -> menu.getType() == MenuTypeEnum.BUTTON.getCode())
                        .collect(Collectors.toList())
                        .stream().map(Menu::getButton).collect(Collectors.toList());

                Map<String,Object> map = new HashMap<>();
                map.put("routes", routes);
                map.put("user", user);
                map.put("button", buttons);
                return map;
            }
        }
        return null;
    }

    private Map<String,Object> queryAdminInfo(User user) {
        //(管理员)查询所有的菜单和按钮
        //查询所有的目录以及菜单
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("type", MenuTypeEnum.DIRECTORY.getCode())
                .or()
                .eq("type", MenuTypeEnum.MENU.getCode());
        List<String> routes = menuMapper.selectList(wrapper).stream().map(Menu::getName).collect(Collectors.toList());

        //获取所有的按钮
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", MenuTypeEnum.BUTTON.getCode());
        List<String> buttons = menuMapper.selectList(queryWrapper).stream().map(Menu::getButton).collect(Collectors.toList());

        Map<String,Object> map = new HashMap<>();
        map.put("routes", routes);
        map.put("user", user);
        map.put("button", buttons);
        return map;
    }

    @Override
    public Map<String,Object> queryUserList(Integer pageNo, Integer pageSize, SearchUserDto dto) {
        Page<User> pageInfo = new Page<User>(pageNo, pageSize);
        dto.setUsername(dto.getUsername().trim());
        dto.setPhone(dto.getPhone().trim());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isBlank(dto.getUsername()),"username",dto.getUsername());
        wrapper.like(!StringUtils.isBlank(dto.getPhone()),"phone",dto.getPhone());
        if(null != dto.getStatus()){
            wrapper.like("status",dto.getStatus());
        }
        wrapper.orderByDesc("create_time");
        Page<User> userPage = baseMapper.selectPage(pageInfo, wrapper);
        if(userPage != null){
            List<User> userList = userPage.getRecords();
            List<UserDto> list = new ArrayList<>();
            if(!CollectionUtils.isEmpty(userList)){
                userList.stream().forEach(user -> {
                    QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("user_id",user.getId());
                    List<Integer> roleIds = userRoleMapper.selectList(queryWrapper).stream().map(UserRole::getRoleId).collect(Collectors.toList());
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getUsername());
                    userDto.setPhone(user.getPhone());
                    userDto.setEmail(user.getEmail());
                    userDto.setGender(user.getGender());
                    userDto.setStatus(user.getStatus());
                    userDto.setCreateTime(user.getCreateTime());
                    if(!CollectionUtils.isEmpty(roleIds)){
                        //用户已经分配过角色
                        List<String> roles = roleMapper.selectBatchIds(roleIds).stream().map(Role::getRoleName).collect(Collectors.toList());
                        userDto.setRoles(roles);
                    }
                    list.add(userDto);
                });
                Map<String,Object> map = new HashMap<>();
                map.put("data", list);
                map.put("total", userPage.getTotal());
                return map;
            }
        }
        return null;
    }

    @Override
    public List<String> queryRoles(Integer userId) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(userRoles)){
            List<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            List<Role> roleList = roleMapper.selectBatchIds(roleIds);
            return roleList.stream().map(Role::getRoleName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoles(String username,List<String> roles) {
        QueryWrapper<User> userWrapper = new QueryWrapper<>();
        userWrapper.eq("username",username);
        User user = baseMapper.selectOne(userWrapper);
        //分配角色之前，将之前拥有的角色先删除后再重新保存新的角色
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",user.getId());
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        if(!CollectionUtils.isEmpty(userRoles)){
            //将原先拥有的角色删除
            List<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",user.getId());
            queryWrapper.in("role_id",roleIds);
            userRoleMapper.delete(queryWrapper);
        }
        //保存新的用户角色
        if(!CollectionUtils.isEmpty(roles)){
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("role_name",roles);
            List<Integer> roleIds = roleMapper.selectList(queryWrapper).stream().map(Role::getId).collect(Collectors.toList());
            roleIds.stream().forEach(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            });
        }
    }

    @Override
    public Map<String, Object> queryRegisterUsers() {
        //查询总人数
        int total = 0;
        List<User> users = baseMapper.selectList(null);
        if(!CollectionUtils.isEmpty(users)) {
            total = users.size();
        }
        //查询昨日新增人数
        int yesterday = baseMapper.queryYesterdayRegisterUsers();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total", total);
        map.put("yesterday", yesterday);
        return map;
    }

    @Override
    public void logout(String token) {
        log.info("用户ID为{}的用户正在请求退出登录...", JwtUtil.parseToken(token));
        if(!StringUtils.isBlank(token)) {
            redisTemplate.delete(RedisConstant.TOKEN_CACHE_KEY + JwtUtil.parseToken(token));
            log.info("用户ID为{}的用户退出登录成功...", JwtUtil.parseToken(token));
        }
    }

    @Override
    public String queryUserNameById(Integer userId) {
        User user = baseMapper.selectById(userId);
        if(user != null){
            return user.getUsername();
        }
        return null;
    }

    @Override
    public void register(RegisterUserDto user) throws SystemException {
        QueryWrapper<User> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("username",user.getUsername());
        User user1 = baseMapper.selectOne(wrapper1);
        if(null != user1){
            throw new SystemException(ResponseCodeEnum.USERNAME_EXITS);
        }
        QueryWrapper<User> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("phone",user.getPhone());
        User user2 = baseMapper.selectOne(wrapper2);
        if(null != user2){
            throw new SystemException(ResponseCodeEnum.PHONE_EXITS);
        }
        QueryWrapper<User> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("email",user.getEmail());
        User user3 = baseMapper.selectOne(wrapper3);
        if(null != user3){
            throw new SystemException(ResponseCodeEnum.EMAIL_EXITS);
        }
        //验证码是否过期
        String code = (String) redisTemplate.opsForValue().get(RedisConstant.MAIL_CODE_CACHE_KEY + user.getEmail());
        if(null == code){
            throw new SystemException(ResponseCodeEnum.CODE_EXPIRED);
        }else{
            if(!code.equalsIgnoreCase(user.getCode())){
                throw new SystemException(ResponseCodeEnum.CODE_ERROR);
            }
        }
        //加密密码
        user.setPassword(SecureUtil.md5(user.getPassword()));
        User saveUser = new User();
        BeanUtils.copyProperties(user, saveUser);
        this.save(saveUser);
        //默认分配路由和按钮权限
        handlerSendRole(user.getUsername());
    }

    @Override
    public UserEditDto echoUserById(Integer userId) {
        User user = baseMapper.selectById(userId);
        UserEditDto userVo = new UserEditDto();
        BeanUtils.copyProperties(user,userVo);
        userVo.setRegisterDate(user.getCreateTime());
        return userVo;
    }

    @Override
    public void updateUserInfo(UserVo userVo) throws SystemException {
        QueryWrapper<User> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("username",userVo.getUsername());
        User user1 = baseMapper.selectOne(wrapper1);
        if((null != user1) && (userVo.getId().intValue() != user1.getId())){
            throw new SystemException(ResponseCodeEnum.USERNAME_EXITS);
        }
        QueryWrapper<User> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("phone",userVo.getPhone());
        User user2 = baseMapper.selectOne(wrapper2);
        if((null != user2) && (userVo.getId().intValue() != user2.getId())){
            throw new SystemException(ResponseCodeEnum.PHONE_EXITS);
        }
        QueryWrapper<User> wrapper3 = new QueryWrapper<>();
        wrapper3.eq("email",userVo.getEmail());
        User user3 = baseMapper.selectOne(wrapper3);
        if((null != user3) && (userVo.getId().intValue() != user3.getId())){
            throw new SystemException(ResponseCodeEnum.EMAIL_EXITS);
        }
        //查看密码是否修改
        User user = baseMapper.selectById(userVo.getId());
        if(null != user){
            if(!user.getPassword().equalsIgnoreCase(userVo.getPassword())){
                user.setPassword(SecureUtil.md5(userVo.getPassword()));
            }
        }
        user.setUsername(userVo.getUsername());
        user.setPhone(userVo.getPhone());
        user.setEmail(userVo.getEmail());
        user.setStatus(userVo.getStatus());
        user.setGender(userVo.getGender());
        this.updateById(user);
    }

    @Override
    public User homeEchoUserById(Integer userId) {
        //对用户的敏感信息进行脱敏处理
        User user = baseMapper.selectById(userId);
        user.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
        user.setEmail(DesensitizedUtil.email(user.getEmail()));
        return user;
    }

    @Override
    public void sendMailCode(String email) {
        try {
            //随机生成6位验证码
            String code = RandomNumberUtils.generateRandomNumber();
            log.info("当前注册邮箱号为：{}，验证码为：{}",email,code);
            SimpleMailMessage mailMessage= new SimpleMailMessage();
            //发送者
            mailMessage.setFrom(this.username);
            //接收者
            mailMessage.setTo(email);
            //邮件标题
            mailMessage.setSubject(MACHINE_TITLE);
            //邮件内容
            mailMessage.setText(TEMPLATE + code + "，请勿泄露于他人！");
            //发送邮箱
            javaMailSender.send(mailMessage);
            //redis缓存3分钟
            redisTemplate.opsForValue().set(RedisConstant.MAIL_CODE_CACHE_KEY + email,code,RedisConstant.MAIL_CODE_CACHE_EXPIRED, TimeUnit.MINUTES);
            log.info("邮箱发送成功：验证码为：{}",code);
        } catch (Exception e) {
            log.info("邮箱发送失败...");
        }
    }

    @Override
    public String emailLogin(RegisterUserDto user) throws SystemException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email",user.getEmail());
        User selectOne = baseMapper.selectOne(wrapper);
        if(selectOne == null){
            throw new SystemException(ResponseCodeEnum.EMAIL_UNREGISTER);
        }
        //查看用户是否禁用
        if(selectOne.getStatus().intValue() == UserStatusEnum.FORBIDDEN.getCode()){
            throw new SystemException(ResponseCodeEnum.USER_FORBIDDEN);
        }
        //验证码是否过期
        String code = (String) redisTemplate.opsForValue().get(RedisConstant.MAIL_CODE_CACHE_KEY + user.getEmail());
        if(null == code){
            throw new SystemException(ResponseCodeEnum.CODE_EXPIRED);
        }else{
            if(!code.equalsIgnoreCase(user.getCode())){
                throw new SystemException(ResponseCodeEnum.CODE_ERROR);
            }
        }
        //获取当前用户所拥有的所有菜单id
        log.info("用户{}正在登录.....",selectOne.getUsername());
        String token = JwtUtil.createToken(selectOne.getId());
        log.info("id为{}的用户的token值为{}",selectOne.getId(),token);
        redisTemplate.opsForValue().set(RedisConstant.TOKEN_CACHE_KEY + selectOne.getId(),token,RedisConstant.TOKEN_CACHE_EXPIRED, TimeUnit.MINUTES);
        return token;
    }

    private void handlerSendRole(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        User user = baseMapper.selectOne(wrapper);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name","普通用户");
        Role role = roleMapper.selectOne(queryWrapper);
        UserRole userRole = new UserRole();
        userRole.setRoleId(role.getId());
        userRole.setUserId(user.getId());
        userRoleMapper.insert(userRole);
    }
}
