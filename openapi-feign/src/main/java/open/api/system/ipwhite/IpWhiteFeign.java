package open.api.system.ipwhite;

import io.swagger.annotations.ApiOperation;
import open.api.response.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: UserFeign
 * Package: open.api.user
 * Description:
 */
@FeignClient(value = "openapi-system")
public interface IpWhiteFeign {

    /**
     * 根据应用key获取设置的IP白名单
     * @param
     * @return
     */
    @GetMapping("/system/ip/white/query/inner/list/{accessKey}")
    List<String> queryIpWhiteListByAccessKey(@PathVariable("accessKey") String accessKey);
}
