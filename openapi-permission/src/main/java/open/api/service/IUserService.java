package open.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import open.api.exception.SystemException;
import open.api.model.dto.RegisterUserDto;
import open.api.model.dto.SearchUserDto;
import open.api.model.dto.UserEditDto;
import open.api.model.entity.User;
import open.api.model.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-04-21
 */
public interface IUserService extends IService<User> {

    /**
     * 用户登录
     * @param user
     */
    String login(User user) throws SystemException;

    Map<String, Object> getUserInfo(Integer userId) throws SystemException;

    /**
     * 分页查询用户数据
     * @param pageNo
     * @param pageSize
     * @param searchDto
     * @return
     */
    Map<String,Object> queryUserList(Integer pageNo, Integer pageSize, SearchUserDto searchDto);

    List<String> queryRoles(Integer userId);

    void saveRoles(String username,List<String> roles);

    /**
     * 首页查询注册用户数据
     * @return
     */
    Map<String, Object> queryRegisterUsers();

    /**
     * 用户退出登录
     * @param token
     */
    void logout(String token);

    /**
     * 根据用户id查询用户名
     * @param userId
     * @return
     */
    String queryUserNameById(Integer userId);

    /**
     * 用户注册
     * @param user
     * @return
     */
    void register(RegisterUserDto user) throws SystemException;

    /**
     * 回显用户数据
     * @param userId
     * @return
     */
    UserEditDto echoUserById(Integer userId);

    /**
     * 修改用户信息
     * @param userVo
     */
    void updateUserInfo(UserVo userVo) throws SystemException;

    /**
     * 首页回显用户数据
     * @param userId
     * @return
     */
    User homeEchoUserById(Integer userId);

    /**
     * 发送邮箱验证码
     * @param email
     */
    void sendMailCode(String email);

    /**
     * 邮箱登录
     * @param user
     * @return
     */
    String emailLogin(RegisterUserDto user) throws SystemException;
}
