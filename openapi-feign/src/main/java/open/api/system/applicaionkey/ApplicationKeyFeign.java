package open.api.system.applicaionkey;

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
public interface ApplicationKeyFeign {

    /**
     * 根据accessKey获取对应的secretKey
     * @return
     */
    @GetMapping("/system/application/key/inner/query/{accessKey}")
    ResultData queryKeyByAccessKey(@PathVariable("accessKey") String accessKey);

    /**
     * 根据accessKey查询所属应用是否处于开启状态
     * @param
     * @return
     */
    @PostMapping("/system/application/key/inner/query/appStatus")
    ResultData queryApplicationStatus(@RequestParam("accessKey") String accessKey);

    /**
     * 根据accessKey查询额度
     * @param
     * @return
     */
    @PostMapping("/system/application/key/inner/query/lines")
    ResultData queryAccessKeyLines(@RequestParam("accessKey") String accessKey);

    /**
     * 根据accessKey将额度-1
     * @param
     * @return
     */
    @PutMapping("/system/application/key/inner/decrease")
    ResultData decreaseLines(@RequestParam("accessKey") String accessKey);

    /**
     * 用户购买额度后根据accessKey修改额度
     * @param
     * @return
     */
    @PutMapping("/system/application/key/inner/update/decrease")
    ResultData updateLinesByAccessKey(@RequestParam("accessKey") String accessKey, @RequestParam("lines") Long lines);

    /**
     * 根据accessKey获取应用key名称
     * @param
     * @return
     */
    @PutMapping("/system/application/key/query/inner/byAccessKey")
    ResultData queryKeyNameByAccessKey(@RequestParam("accessKey") String accessKey);

    /**
     * 根据用户Id查询key名称
     * @return
     */
    @GetMapping("/system/application/key/query/inner/user/keyName/{userId}")
    ResultData queryKeyNameByUserId(@PathVariable("userId") Integer userId);
}
