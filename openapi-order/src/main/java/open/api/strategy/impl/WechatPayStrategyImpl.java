package open.api.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import open.api.model.entity.Pay;
import open.api.strategy.StrategyFactory;
import open.api.strategy.factory.PayFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: WechatPayStrategyImpl
 * Package: open.api.strategy.impl
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/5/5 - 16:16
 * @Version: v1.0
 */
/**
 * 微信支付具体实现
 */
@Slf4j
@Component
public class WechatPayStrategyImpl implements StrategyFactory {
    /**
     * 支付类型
     */
    private static final String PAY_TYPE = "wxPay";

    @Override
    public Map<String, Object> pay(Pay pay) {
        //TODO: 未对接微信支付：具体支付实现逻辑
        System.out.println("微信支付" + pay.toString());
        return new HashMap<>();
    }

    /**
     * 注册微信支付策略方法
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        PayFactory.register(PAY_TYPE,this);
    }
}
