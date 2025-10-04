package open.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.dto.InterfaceInfoSaveDto;
import open.api.model.dto.InterfaceInfoSearchDto;
import open.api.model.entity.Plan;
import open.api.response.ResultData;
import open.api.service.IPlanService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 额度套餐表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-05
 */
@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/lines/plan")
@Api(tags = "配额管理模块")
@RequiredArgsConstructor
public class PlanController {

    private final IPlanService planService;

    /**
     * 查询套餐数据
     * @param
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询套餐数据")
    public ResultData queryPlanList(String name) {
        List<Plan> list = planService.queryPlanList(name);
        return ResultData.success(list);
    }

    /**
     * 保存套餐信息
     x* @param saveDto
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存套餐信息")
    public ResultData savePlan(@RequestBody @Valid Plan plan) throws SystemException {
        planService.save(plan);
        return ResultData.success();
    }

    /**
     * 修改套餐信息
     x* @param saveDto
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation(value = "修改套餐信息")
    public ResultData editPlan(@RequestBody @Valid Plan plan) throws SystemException {
        planService.updateById(plan);
        return ResultData.success();
    }

    /**
     * 回显套餐信息
     x* @param saveDto
     * @return
     */
    @GetMapping("/echo/{id}")
    @ApiOperation(value = "回显套餐信息")
    public ResultData echoPlan(@PathVariable("id") Integer id) throws SystemException {
        Plan plan = planService.getById(id);
        return ResultData.success(plan);
    }

    /**
     * TODO: 测试环境下禁用 删除套餐信息
     x* @param saveDto
     * @return
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除套餐信息")
    public ResultData removePlan(@PathVariable("id") Integer id) throws SystemException {
        // planService.removeById(id);
        return ResultData.success("模拟删除成功~");
    }
}
