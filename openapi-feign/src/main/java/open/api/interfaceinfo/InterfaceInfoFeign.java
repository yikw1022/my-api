package open.api.interfaceinfo;

import open.api.response.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
public interface InterfaceInfoFeign {

    /**
     * 根据accessKey获取对应的secretKey
     * @return
     */
    @PutMapping("/interface/info/inner/increment")
    ResultData incrementInterfaceCount(@RequestParam("encodeUrl") String encodeUrl);

    /**
     * 根据接口url查询接口是否存在
     * @param
     * @return
     */
    @PostMapping("/interface/info/inner/query/exists/byUrl")
    ResultData queryInterfaceInfoByUrl(@RequestParam("encodeUrl") String encodeUrl);

    /**
     * 根据接口url查询接口是否在线
     * @param
     * @return
     */
    @PostMapping("/interface/info/inner/query/status/byUrl")
    ResultData queryInterfaceInfoStatusByUrl(@RequestParam("encodeUrl") String encodeUrl);
}
