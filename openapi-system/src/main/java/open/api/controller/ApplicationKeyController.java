package open.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.entity.Application;
import open.api.model.entity.ApplicationKey;
import open.api.model.vo.ApplicationCreateVo;
import open.api.model.vo.ApplicationKeyAddVo;
import open.api.response.ResultData;
import open.api.service.IApplicationKeyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 应用与key联表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-04-27
 */
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@Api(tags = "应用管理模块")
@RestController
@RequestMapping("/system/application/key")
public class ApplicationKeyController {

    private final IApplicationKeyService applicationKeyService;

    /**
     * 添加key保存
     * @param
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加key保存")
    public ResultData saveApplicationKey(@RequestBody ApplicationKeyAddVo keyAddVo) throws SystemException {
        applicationKeyService.saveApplicationKey(keyAddVo);
        return ResultData.success();
    }

    /**
     * 根据应用Id查询Key数据
     * @param
     * @return
     */
    @GetMapping("/queryKey/{applicationId}")
    @ApiOperation(value = "根据应用Id查询Key数据")
    public ResultData queryKeysByApplicationId(@PathVariable("applicationId") Integer applicationId) throws SystemException {
        List<ApplicationKey> list = applicationKeyService.queryKeysByApplicationId(applicationId);
        return ResultData.success(list);
    }

    /**
     * 根据用户Id查询Key数据
     * @param
     * @return
     */
    @GetMapping("/queryKey/byUserId/{userId}")
    @ApiOperation(value = "根据用户Id查询Key数据")
    public ResultData queryKeysByUserId(@PathVariable("userId") Integer userId) throws SystemException {
        List<ApplicationKey> list = applicationKeyService.queryKeysByUserId(userId);
        return ResultData.success(list);
    }

    /**
     * 根据accessKey查询所属应用是否处于开启状态
     * @param
     * @return
     */
    @PostMapping("/inner/query/appStatus")
    @ApiOperation(value = "根据accessKey查询所属应用是否处于开启状态")
    public ResultData queryApplicationStatus(@RequestParam("accessKey") String accessKey) throws SystemException {
        boolean status = applicationKeyService.queryApplicationStatus(accessKey);
        return ResultData.success(status);
    }

    /**
     * 根据accessKey将额度-1
     * @param
     * @return
     */
    @PutMapping("/inner/decrease")
    @ApiOperation(value = "根据accessKey将额度-1")
    public ResultData decreaseLines(@RequestParam("accessKey") String accessKey) throws SystemException {
        boolean decreased = applicationKeyService.decreaseLines(accessKey);
        return ResultData.success(decreased);
    }

    /**
     * 根据accessKey获取应用key名称
     * @param
     * @return
     */
    @PutMapping("/query/inner/byAccessKey")
    @ApiOperation(value = "根据accessKey获取应用key名称")
    public ResultData queryKeyNameByAccessKey(@RequestParam("accessKey") String accessKey) throws SystemException {
        String keyName = applicationKeyService.queryKeyNameByAccessKey(accessKey);
        return ResultData.success(keyName);
    }

    /**
     * 用户购买额度后根据accessKey修改额度
     * @param
     * @return
     */
    @PutMapping("/inner/update/decrease")
    @ApiOperation(value = "用户购买额度后根据accessKey修改额度")
    public ResultData updateLinesByAccessKey(@RequestParam("accessKey") String accessKey,
                                             @RequestParam("lines") Long lines) throws SystemException {
        boolean decreased = applicationKeyService.updateLinesByAccessKey(accessKey,lines);
        return ResultData.success(decreased);
    }

    /**
     * 根据accessKey查询额度
     * @param
     * @return
     */
    @PostMapping("/inner/query/lines")
    @ApiOperation(value = "根据accessKey查询额度")
    public ResultData queryAccessKeyLines(@RequestParam("accessKey") String accessKey) throws SystemException {
        boolean status = applicationKeyService.queryAccessKeyLines(accessKey);
        return ResultData.success(status);
    }

    /**
     * TODO: 测试环境下禁用 删除key
     * @param
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "删除key")
    public ResultData deleteKeyById(@PathVariable("id") Integer id) throws SystemException {
        // applicationKeyService.deleteKeyById(id);
        return ResultData.success("模拟删除成功~");
    }

    /**
     * 根据accessKey获取对应的secretKey
     * @param
     * @return
     */
    @GetMapping("/inner/query/{accessKey}")
    @ApiOperation(value = "查询secretKey")
    public ResultData queryKeyByAccessKey(@PathVariable("accessKey") String accessKey) throws SystemException {
        String secretKey = applicationKeyService.queryKeyByAccessKey(accessKey);
        return ResultData.success(secretKey);
    }

    /**
     * 首页根据用户Id查询创建应用Key数据
     * @return
     */
    @GetMapping("/query/user/number/{userId}")
    @ApiOperation(value = "首页根据用户Id查询创建应用Key数据")
    public ResultData queryApplicationKeysByUserId(@PathVariable("userId") Integer userId){
        int count = applicationKeyService.queryApplicationKeysByUserId(userId);
        return ResultData.success(count);
    }

    /**
     * 根据用户Id查询key名称
     * @return
     */
    @GetMapping("/query/inner/user/keyName/{userId}")
    @ApiOperation(value = "根据用户Id查询key名称")
    public ResultData queryKeyNameByUserId(@PathVariable("userId") Integer userId){
        List<String> list = applicationKeyService.queryKeyNameByUserId(userId);
        return ResultData.success(list);
    }
}
