package open.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.entity.ApplicationKey;
import open.api.model.entity.IpWhite;
import open.api.response.ResultData;
import open.api.service.IIpWhiteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * ip白名单表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@Api(tags = "白名单管理模块")
@RestController
@RequestMapping("/system/ip/white")
public class IpWhiteController {

    private final IIpWhiteService whiteService;

    /**
     * 根据keyId的查询IP白名单数据
     * @param
     * @return
     */
    @GetMapping("/list/{keyId}")
    @ApiOperation(value = "根据keyId的查询IP白名单数据")
    public ResultData queryIpWhiteList(@PathVariable("keyId") Integer keyId){
        List<IpWhite> list = whiteService.queryIpWhiteList(keyId);
        return ResultData.success(list);
    }

    /**
     * 保存IP白名单数据
     * @param
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存IP白名单数据")
    public ResultData saveIpWhiteList(@RequestBody IpWhite ipWhite) throws SystemException {
        whiteService.saveIpWhiteList(ipWhite);
        return ResultData.success();
    }

    /**
     * TODO: 测试环境下禁用 删除IP白名单
     * @param
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "删除IP白名单")
    public ResultData delete(@PathVariable("id") Integer id){
        // whiteService.removeById(id);
        return ResultData.success("模拟删除成功~");
    }

    /**
     * 根据应用key获取设置的IP白名单
     * @param
     * @return
     */
    @GetMapping("/query/inner/list/{accessKey}")
    @ApiOperation(value = "根据应用key获取设置的IP白名单")
    public List<String> queryIpWhiteListByAccessKey(@PathVariable("accessKey") String accessKey){
        List<String> ipWhiteList = whiteService.queryIpWhiteListByAccessKey(accessKey);
        return ipWhiteList;
    }
}
