package open.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.entity.Application;
import open.api.model.vo.ApplicationCreateVo;
import open.api.model.vo.ApplicationListVo;
import open.api.response.ResultData;
import open.api.service.IApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 应用管理表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-04-27
 */
@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@Api(tags = "应用管理模块")
@RequestMapping("/system/application")
public class ApplicationController {

    private final IApplicationService applicationService;

    /**
     * 获取用户应用数据
     * @param
     * @return
     */
    @GetMapping("/app/list/{userId}")
    @ApiOperation(value = "获取用户应用数据")
    public ResultData queryApplicationList(@PathVariable("userId") Integer userId) throws SystemException {
        List<ApplicationListVo> list = applicationService.queryApplicationList(userId);
        return ResultData.success(list);
    }

    /**
     * 回显应用数据
     * @param
     * @return
     */
    @GetMapping("/app/info/{id}")
    @ApiOperation(value = "回显应用数据")
    public ResultData queryAppInfoById(@PathVariable("id") Integer id) throws SystemException {
        Application application = applicationService.getById(id);
        return ResultData.success(application);
    }

    /**
     * 创建新应用
     * @param
     * @return
     */
    @PostMapping("/create/save")
    @ApiOperation(value = "创建新应用")
    public ResultData saveApplication(@RequestBody ApplicationCreateVo createVo) throws SystemException {
        applicationService.saveApplication(createVo);
        return ResultData.success();
    }

    /**
     * 修改应用
     * @param
     * @return
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改应用")
    public ResultData editApplication(@RequestBody ApplicationCreateVo createVo) throws SystemException {
        applicationService.editApplication(createVo);
        return ResultData.success();
    }

    /**
     * TODO: 测试环境下禁用 删除应用
     * @param
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "删除应用")
    public ResultData deleteApplicationById(@PathVariable("id") Integer id) throws SystemException {
        // applicationService.removeById(id);
        return ResultData.success("模拟删除成功~");
    }

    /**
     * 修改应用状态
     * @param
     * @return
     */
    @PutMapping("/update/status/{id}")
    @ApiOperation(value = "修改应用状态")
    public ResultData updateAppStatus(@PathVariable("id") Integer id) throws SystemException {
        applicationService.updateAppStatus(id);
        return ResultData.success();
    }

    /**
     * 首页查询创建应用数据
     * @return
     */
    @GetMapping("/query/number")
    @ApiOperation(value = "首页查询创建应用数据")
    public ResultData queryApplications(){
        Map<String,Object> map = applicationService.queryApplications();
        return ResultData.success(map);
    }

    /**
     * 首页根据用户Id查询创建应用数据
     * @return
     */
    @GetMapping("/query/user/number/{userId}")
    @ApiOperation(value = "首页根据用户Id查询创建应用数据")
    public ResultData queryApplicationsByUserId(@PathVariable("userId") Integer userId){
        int count = applicationService.queryApplicationsByUserId(userId);
        return ResultData.success(count);
    }
}
