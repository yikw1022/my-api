package open.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.model.entity.UserInterfaceInfo;
import open.api.model.vo.InvokeDetailVo;
import open.api.response.ResultData;
import open.api.service.IUserInterfaceInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户调用接口次数信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-08
 */
@Slf4j
@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@Api(tags = "用户-接口管理模块")
@RequestMapping("/interface/user/info")
public class UserInterfaceInfoController {

    private final IUserInterfaceInfoService userInterfaceInfoService;

    /**
     * 保存用户调用接口的信息
     * @return
     */
    @PostMapping("/inner/save")
    @ApiOperation(value = "保存用户调用接口的信息")
    public ResultData saveUserInterfaceInfo(@RequestBody UserInterfaceInfo interfaceInfo){
        boolean save = userInterfaceInfoService.saveUserInterfaceInfo(interfaceInfo);
        return ResultData.success(save);
    }

    /**
     * 获取用户的今日调用接口次数
     * @return
     */
    @GetMapping("/invoke/count/{userId}")
    @ApiOperation(value = "获取用户的今日调用接口次数")
    public ResultData queryInvokeCountByUserId(@PathVariable("userId") Integer userId){
        Map<String,Object> map = userInterfaceInfoService.queryInvokeCountByUserId(userId);
        return ResultData.success(map);
    }

    /**
     * 获取普通用户的首页key调用接口数据详情
     * @return
     */
    @GetMapping("/query/detail/invoke/{userId}")
    @ApiOperation(value = "获取普通用户的首页key调用接口数据详情")
    public ResultData queryInvokeDetailByUserId(@PathVariable("userId") Integer userId){
        List<InvokeDetailVo> list = userInterfaceInfoService.queryInvokeDetailByUserId(userId);
        return ResultData.success(list);
    }

    /**
     * 获取普通用户的流量详情数据(24小时)
     * @return
     */
    @GetMapping("/query/interface/traffic/{userId}")
    @ApiOperation(value = "获取普通用户的流量详情数据(24小时)")
    public ResultData queryInterfaceTrafficByUserId(@PathVariable("userId") Integer userId){
        List<InvokeDetailVo> list = userInterfaceInfoService.queryInterfaceTrafficByUserId(userId);
        return ResultData.success(list);
    }

    /**
     * 获取普通用户的流量详情数据(30天)
     * @return
     */
    @GetMapping("/query/thirtyDay/traffic/{userId}")
    @ApiOperation(value = "获取普通用户的流量详情数据(30天)")
    public ResultData queryThirtyDayTrafficByUserId(@PathVariable("userId") Integer userId){
        List<InvokeDetailVo> list = userInterfaceInfoService.queryThirtyDayTrafficByUserId(userId);
        return ResultData.success(list);
    }

    /**
     * 获取普通用户的流量详情数据(6个月)
     * @return
     */
    @GetMapping("/query/six/month/traffic/{userId}")
    @ApiOperation(value = "获取普通用户的流量详情数据(6个月)")
    public ResultData querySixMonthTrafficByUserId(@PathVariable("userId") Integer userId){
        List<InvokeDetailVo> list = userInterfaceInfoService.querySixMonthTrafficByUserId(userId);
        return ResultData.success(list);
    }

    /**
     * TODO: 测试环境下禁用 根据key_name删除调用接口记录
     * @return
     */
    @DeleteMapping("/inner/delete/{keyName}")
    @ApiOperation(value = "根据key_name删除调用接口记录")
    public ResultData deleteBykeyName(@PathVariable("keyName") String keyName){
        // userInterfaceInfoService.deleteBykeyName(keyName);
        return ResultData.success("模拟删除成功~");
    }
}
