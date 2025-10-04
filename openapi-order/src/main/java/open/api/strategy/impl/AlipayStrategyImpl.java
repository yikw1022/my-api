package open.api.strategy.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.mapper.OrderInfoMapper;
import open.api.model.entity.Pay;
import open.api.response.ResponseCodeEnum;
import open.api.service.IOrderInfoService;
import open.api.strategy.StrategyFactory;
import open.api.strategy.factory.PayFactory;
import open.api.utils.GenerateQRcodeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: AlipayStrategyImpl
 * Package: open.api.strategy.impl
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/5/5 - 16:15
 * @Version: v1.0
 */
/**
 * 支付宝支付具体实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlipayStrategyImpl implements StrategyFactory{

    /**
     * 支付类型
     */
    private static final String PAY_TYPE = "aliPay";

    private final AlipayConfig alipayConfig;

    private final IOrderInfoService orderInfoService;

    @Override
    public Map<String,Object> pay(Pay pay) {
        log.info("{}支付请求进来了-----------",pay.getPaymentType());
        //设置支付宝支付参数
        pay.setName(pay.getPrice() + "元额度套餐");
        return handleAliPay(pay);
    }

    private Map<String, Object> handleAliPay(Pay pay) {
        AlipayClient alipayClient = null;
        Map<String,Object> map = null;
        try {
            alipayClient = new DefaultAlipayClient(alipayConfig);
            AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            model.setOutTradeNo(pay.getOrderId());
            model.setTotalAmount(pay.getPrice());
            model.setSubject(pay.getName());
            request.setBizModel(model);
            AlipayTradePrecreateResponse res = alipayClient.execute(request);
            log.info("支付宝发起支付请求返回结果：{}",res);
            if (res.isSuccess()) {
                log.info("订单号为：{}的订单发起支付请求成功...", pay.getOrderId());
                map = new HashMap<>(2);
                map.put("tradeNo",pay.getOrderId());
                map.put("payUrl", GenerateQRcodeUtil.generateQRCode(res.getQrCode()));

                return map;
            } else {
                log.error("订单号为：{}的订单发起支付请求支付失败！！！", pay.getOrderId());
            }
        } catch (AlipayApiException e) {
            log.error("支付宝发起支付请求异常", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 注册支付宝支付策略方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        PayFactory.register(PAY_TYPE,this);
    }
}
