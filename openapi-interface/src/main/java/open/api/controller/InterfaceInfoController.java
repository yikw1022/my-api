package open.api.controller;


import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.dto.InterfaceInfoSaveDto;
import open.api.model.dto.InterfaceInfoSearchDto;
import open.api.model.dto.OnlineInvokeDto;
import open.api.model.dto.PublishDto;
import open.api.model.entity.Application;
import open.api.model.entity.InterfaceInfo;
import open.api.model.vo.ApplicationCreateVo;
import open.api.model.vo.InterfaceInfoSaveVo;
import open.api.model.vo.InvokeDetailVo;
import open.api.response.ResultData;
import open.api.service.IInterfaceInfoService;
import open.api.utils.EncodeUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 接口信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-04-26
 */
@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/interface/info")
@Api(tags = "接口管理模块")
public class InterfaceInfoController {

    private final IInterfaceInfoService interfaceInfoService;

    /**
     * 分页查询接口数据
     * @param
     * @return
     */
    @GetMapping("/list/{pageNo}/{pageSize}")
    @ApiOperation(value = "分页查询接口数据")
    public ResultData queryInterfaceList(@PathVariable("pageNo") Integer pageNo,
                                    @PathVariable("pageSize") Integer pageSize,
                                    InterfaceInfoSearchDto searchDto) {
        Map<String,Object> map = interfaceInfoService.queryInterfaceList(pageNo,pageSize,searchDto);
        return ResultData.success(map);
    }

    /**
     * 分页查询接口数据(管理员)
     * @param
     * @return
     */
    @GetMapping("/admin/list/{pageNo}/{pageSize}")
    @ApiOperation(value = "分页查询接口数据(管理员)")
    public ResultData queryAdminInterfaceList(@PathVariable("pageNo") Integer pageNo,
                                         @PathVariable("pageSize") Integer pageSize,
                                         InterfaceInfoSearchDto searchDto) {
        Map<String,Object> map = interfaceInfoService.queryAdminInterfaceList(pageNo,pageSize,searchDto);
        return ResultData.success(map);
    }

    /**
     * 保存接口信息
     x* @param saveDto
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存接口信息")
    public ResultData saveInterfaceInfo(@RequestBody @Valid InterfaceInfoSaveDto saveDto) throws SystemException {
        interfaceInfoService.saveInterfaceInfo(saveDto);
        return ResultData.success();
    }

    /**
     * 发布接口
     * @param
     * @return
     */
    @PostMapping("/publish")
    @ApiOperation(value = "发布接口")
    public ResultData publishInterface(@RequestBody PublishDto publishDto) throws SystemException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return interfaceInfoService.publishInterface(publishDto);
    }

    /**
     * 在线调用接口测试
     * @param
     * @return
     */
    @PostMapping("/online/invoke")
    @ApiOperation(value = "在线调用接口测试")
    public ResultData onlineInvokeInterface(@RequestBody OnlineInvokeDto OnlineInvokeDto) throws SystemException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return interfaceInfoService.onlineInvokeInterface(OnlineInvokeDto);
    }

    /**
     * 下线接口
     * @param
     * @return
     */
    @GetMapping("line/{id}")
    @ApiOperation(value = "下线接口")
    public ResultData lineInterface(@PathVariable("id") Integer id) throws SystemException {
        interfaceInfoService.lineInterface(id);
        return ResultData.success();
    }

    /**
     * 接口调用次数+1
     * @param
     * @return
     */
    @PutMapping("/inner/increment")
    @ApiOperation(value = "接口调用次数加1")
    public ResultData incrementInterfaceCount(@RequestParam("encodeUrl") String encodeUrl) throws SystemException {
        String url = EncodeUtil.decodeBody(encodeUrl);
        Map<String,Object> map = interfaceInfoService.incrementInterfaceCount(url);
        return ResultData.success(map);
    }

    /**
     * 根据接口url查询接口是否存在
     * @param
     * @return
     */
    @PostMapping("/inner/query/exists/byUrl")
    @ApiOperation(value = "根据接口url查询接口是否存在")
    public ResultData queryInterfaceInfoByUrl(@RequestParam("encodeUrl") String encodeUrl) throws SystemException {
        String url = EncodeUtil.decodeBody(encodeUrl);
        boolean isExists = interfaceInfoService.queryInterfaceInfoByUrl(url);
        return ResultData.success(isExists);
    }

    /**
     * 根据接口url查询接口是否在线
     * @param
     * @return
     */
    @PostMapping("/inner/query/status/byUrl")
    @ApiOperation(value = "根据接口url查询接口是否在线")
    public ResultData queryInterfaceInfoStatusByUrl(@RequestParam("encodeUrl") String encodeUrl) throws SystemException {
        String url = EncodeUtil.decodeBody(encodeUrl);
        boolean status = interfaceInfoService.queryInterfaceInfoStatusByUrl(url);
        return ResultData.success(status);
    }

    /**
     * 回显接口数据
     * @param
     * @return
     */
    @GetMapping("/echo/{id}")
    @ApiOperation(value = "回显接口数据")
    public ResultData echoInterfaceInfoById(@PathVariable("id") Integer id) throws SystemException {
        InterfaceInfoSaveVo saveVo = interfaceInfoService.echoInterfaceInfoById(id);
        return ResultData.success(saveVo);
    }

    /**
     * 修改接口
     * @param
     * @return
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改接口")
    public ResultData editInterface(@RequestBody @Valid InterfaceInfoSaveVo infoSaveVo) throws SystemException {
        interfaceInfoService.editInterface(infoSaveVo);
        return ResultData.success();
    }

    /**
     * TODO: 测试环境下禁用 删除接口
     * @param
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "删除接口")
    public ResultData deleteInterfaceById(@PathVariable("id") Integer id) throws SystemException {
        // interfaceInfoService.deleteInterfaceById(id);
        return ResultData.success("模拟删除成功~");
    }

    /**
     * 首页查询接口数据
     * @return
     */
    @GetMapping("/query/number")
    @ApiOperation(value = "首页查询接口数据")
    public ResultData queryInterfaces(){
        Map<String,Object> map = interfaceInfoService.queryInterfaces();
        return ResultData.success(map);
    }

    /**
     * 查询首页Top 5热点api调用次数
     * @param
     * @return
     */
    @GetMapping("/query/top/five")
    @ApiOperation(value = "查询首页Top 5热点api调用次数")
    public ResultData queryTopFive() {
        Map<String,Object> map = interfaceInfoService.queryTopFive();
        return ResultData.success(map);
    }

    /**
     * 查询首页Top 10活跃用户
     * @param
     * @return
     */
    @GetMapping("/query/top/ten/user")
    @ApiOperation(value = "查询首页Top 10活跃用户")
    public ResultData queryTopTenUser() {
        List<Map<String,Object>> list = interfaceInfoService.queryTopTenUser();
        return ResultData.success(list);
    }
}
