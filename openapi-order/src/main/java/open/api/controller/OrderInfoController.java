package open.api.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.model.dto.SaveOrderInfoDto;
import open.api.model.entity.Pay;
import open.api.model.vo.QueryOrderInfoVo;
import open.api.response.ResponseCodeEnum;
import open.api.response.ResultData;
import open.api.service.IOrderInfoService;
import open.api.strategy.StrategyFactory;
import open.api.strategy.factory.PayFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-05
 */
@Slf4j
@Validated
@CrossOrigin
@RestController
@Api(tags = "配额管理模块")
@RequiredArgsConstructor
@RequestMapping("/lines/order/info")
public class OrderInfoController {

    private final IOrderInfoService orderInfoService;

    /**
     * 这是不对接任何支付渠道，只是模拟支付接口，后续对接支付渠道，可以直接删除
     x* @param saveDto
     * @return
     */
    @PostMapping("/test/save")
    @ApiOperation(value = "模拟保存订单信息，获取订单Id")
    public ResultData testSaveOrderInfo(@RequestBody @Valid SaveOrderInfoDto orderInfoDto) throws SystemException, open.api.exception.SystemException {
        orderInfoService.testSaveOrderInfo(orderInfoDto);
        return ResultData.success();
    }

    /**
     * 保存订单信息，获取订单Id
     x* @param saveDto
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存订单信息，获取订单Id")
    public ResultData saveOrderInfo(@RequestBody @Valid SaveOrderInfoDto orderInfoDto) throws SystemException, open.api.exception.SystemException {
        String orderId = orderInfoService.saveOrderInfo(orderInfoDto);
        return ResultData.success(orderId);
    }

    /**
     * 真实支付接口
     * @return
     */
    @PostMapping("/pay")
    @ApiOperation(value = "支付接口")
    public ResultData commonPay(@RequestBody @Valid Pay pay) throws SystemException {
        StrategyFactory invokeStrategy = PayFactory.getInvokeStrategy(pay.getPaymentType());
        Map<String, Object> result = invokeStrategy.pay(pay);
        return ResultData.success(result);
    }

    /**
     * 查询订单支付状态
     */
    @PostMapping("/query")
    public ResultData queryOrderInfoStatus(@RequestParam("orderId") String orderId, HttpServletRequest request){
        boolean flag = orderInfoService.queryOrderInfoStatus(orderId, request);
        return ResultData.success(flag);
    }

    /**
     * 根据用户id获取订单数据
     */
    @GetMapping("/query/order/{userId}/{type}")
    @ApiOperation(value = "根据用户id获取订单数据")
    public ResultData queryOrderByUserId(@PathVariable("userId") Integer userId,
                                         @PathVariable("type") Integer type,
                                         String orderId){
        List<QueryOrderInfoVo> list = orderInfoService.queryOrderByUserId(userId,type,orderId);
        return ResultData.success(list);
    }

    /**
     * 首页查询订单数据
     * @return
     */
    @GetMapping("/query/number")
    @ApiOperation(value = "首页查询订单数据")
    public ResultData queryOrders(){
        Map<String,Object> map = orderInfoService.queryOrders();
        return ResultData.success(map);
    }

    /**
     * 修改订单数据
     * @return
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改订单数据")
    public ResultData updateOrderInfo(@RequestBody QueryOrderInfoVo vo){
        orderInfoService.updateOrderInfo(vo);
        return ResultData.success();
    }

    /**
     * 回显订单数据
     * @return
     */
    @GetMapping("/echo/{id}")
    @ApiOperation(value = "回显订单数据")
    public ResultData echoOrderInfo(@PathVariable("id") Integer id){
        QueryOrderInfoVo vo = orderInfoService.echoOrderInfo(id);
        return ResultData.success(vo);
    }

    /**
     * TODO: 测试环境下禁用 删除订单
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "删除订单")
    public ResultData deleteOrder(@PathVariable("id") Integer id){
        orderInfoService.removeById(id);
        return ResultData.success("模拟删除成功~");
    }
}
