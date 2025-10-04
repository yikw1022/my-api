package open.api.userinterfaceinfo;

import io.swagger.annotations.ApiOperation;
import open.api.model.entity.UserInterfaceInfo;
import open.api.response.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * ClassName: UserFeign
 * Package: open.api.user
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/4/28 - 9:23
 * @Version: v1.0
 */
@FeignClient(value = "openapi-interface")
public interface UserInterfaceInfoFeign {

    /**
     * 保存用户调用接口的信息
     * @return
     */
    @PostMapping("/interface/user/info/inner/save")
    ResultData saveUserInterfaceInfo(@RequestBody UserInterfaceInfo interfaceInfo);

    /**
     * 根据key_name删除调用接口记录
     * @return
     */
    @DeleteMapping("/interface/user/info/inner/delete/{keyName}")
    ResultData deleteBykeyName(@PathVariable("keyName") String keyName);
}
