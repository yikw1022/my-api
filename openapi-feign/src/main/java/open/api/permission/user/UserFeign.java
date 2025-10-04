package open.api.permission.user;

import open.api.response.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ClassName: UserFeign
 * Package: open.api.user
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/4/28 - 9:23
 * @Version: v1.0
 */
@FeignClient(value = "openapi-permission")
public interface UserFeign {

    /**
     * 获取用户角色列表
     * @return
     */
    @GetMapping("/permission/user/roles/{userId}")
    ResultData queryRoles(@PathVariable("userId") Integer userId);

    /**
     * 根据用户id查询用户名
     * @return
     */
    @GetMapping("/permission/user/query/inner/username/{userId}")
    ResultData queryUserNameById(@PathVariable("userId") Integer userId);
}
