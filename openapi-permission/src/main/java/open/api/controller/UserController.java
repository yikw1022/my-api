package open.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.dto.RegisterUserDto;
import open.api.model.dto.SearchUserDto;
import open.api.model.dto.UserDto;
import open.api.model.dto.UserEditDto;
import open.api.model.entity.User;
import open.api.model.vo.UserVo;
import open.api.response.ResultData;
import open.api.service.IUserService;
import open.api.utils.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ClassName: TestController
 * Package: org.common.contronller
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/4/21 - 13:29
 * @Version: v1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/permission/user")
@RequiredArgsConstructor
@Api(tags = "用户管理模块")
public class UserController {

    private final IUserService userService;

    /**
     * 分页查询用户数据
     * @param
     * @return
     */
    @GetMapping("/list/{pageNo}/{pageSize}")
    @ApiOperation(value = "分页查询用户数据")
    public ResultData queryUserList(@PathVariable("pageNo") Integer pageNo,
                                    @PathVariable("pageSize") Integer pageSize,
                                    SearchUserDto searchDto) {
        Map<String,Object> map = userService.queryUserList(pageNo,pageSize,searchDto);
        return ResultData.success(map);
    }

    /**
     * 用户登录
     * @param user
     * @return
     * @throws SystemException
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public ResultData login(@RequestBody User user) throws SystemException {
       String token = userService.login(user);
        return ResultData.success(token);
    }

    /**
     * 邮箱登录
     * @param user
     * @return
     * @throws SystemException
     */
    @PostMapping("/login/email")
    @ApiOperation(value = "邮箱登录")
    public ResultData emailLogin(@RequestBody RegisterUserDto user) throws SystemException {
        String token = userService.emailLogin(user);
        return ResultData.success(token);
    }

    /**
     * 用户注册
     * @param user
     * @return
     * @throws SystemException
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public ResultData register(@RequestBody RegisterUserDto user) throws SystemException {
        userService.register(user);
        return ResultData.success();
    }

    /**
     * 修改用户信息
     * @return
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改用户信息")
    public ResultData updateUserInfo(@RequestBody UserVo userVo) throws SystemException {
        userService.updateUserInfo(userVo);
        return ResultData.success();
    }

    /**
     * 用户退出登录
     * @throws SystemException
     */
    @PostMapping("/logout")
    @ApiOperation(value = "用户退出登录")
    public ResultData logout(@RequestParam("token") String token) {
        userService.logout(token);
        return ResultData.success();
    }

    /**
     * 获取用户信息
     * @return
     * @throws SystemException
     */
    @PostMapping("/info")
    @ApiOperation(value = "获取用户信息")
    public ResultData userInfo(@RequestParam("token") String token) throws SystemException {
        Integer userId = JwtUtil.parseToken(token);
        Map<String,Object> map = userService.getUserInfo(userId);
        return ResultData.success(map);
    }

    /**
     * 获取用户角色列表
     * @return
     * @throws SystemException
     */
    @GetMapping("/roles/{userId}")
    @ApiOperation(value = "获取用户角色列表")
    public ResultData queryRoles(@PathVariable("userId") Integer userId) throws SystemException {
        List<String> roles = userService.queryRoles(userId);
        return ResultData.success(roles);
    }

    /**
     * 保存已分配的用户角色
     * @param
     * @return
     * @throws SystemException
     */
    @PostMapping("/save/roles/{username}")
    @ApiOperation(value = "保存已分配的用户角色")
    public ResultData saveRoles(@PathVariable("username") String username,
                                @RequestBody List<String> roles) {
        userService.saveRoles(username,roles);
        return ResultData.success();
    }

    /**
     * 首页查询注册用户数据
     * @return
     */
    @GetMapping("/query/register")
    @ApiOperation(value = "首页查询注册用户数据")
    public ResultData queryRegisterUsers(){
        Map<String,Object> map = userService.queryRegisterUsers();
        return ResultData.success(map);
    }

    /**
     * 根据用户id查询用户名
     * @return
     */
    @GetMapping("/query/inner/username/{userId}")
    @ApiOperation(value = "根据用户id查询用户名")
    public ResultData queryUserNameById(@PathVariable("userId") Integer userId){
        String username = userService.queryUserNameById(userId);
        return ResultData.success(username);
    }

    /**
     * TODO: 测试环境下禁用 删除用户
     * @return
     */
    @DeleteMapping("/delete/{userId}")
    @ApiOperation(value = "删除用户")
    public ResultData deleteByUserId(@PathVariable("userId") Integer userId){
        // userService.removeById(userId);
        return ResultData.success("模拟删除成功~");
    }

    /**
     * 首页回显用户数据
     * @return
     */
    @GetMapping("/home/echo/{userId}")
    @ApiOperation(value = "首页回显用户数据")
    public ResultData homeEchoUserById(@PathVariable("userId") Integer userId){
        User user = userService.homeEchoUserById(userId);
        return ResultData.success(user);
    }

    /**
     * 回显用户数据
     * @return
     */
    @GetMapping("/echo/{userId}")
    @ApiOperation(value = "回显用户数据")
    public ResultData echoUserById(@PathVariable("userId") Integer userId){
        UserEditDto userVo = userService.echoUserById(userId);
        return ResultData.success(userVo);
    }

    /**
     * 发送邮箱验证码
     * @return
     */
    @GetMapping("/send/code")
    @ApiOperation(value = "发送邮箱验证码")
    public ResultData sendMailCode(@RequestParam("email") String email){
        userService.sendMailCode(email);
        return ResultData.success();
    }
}
