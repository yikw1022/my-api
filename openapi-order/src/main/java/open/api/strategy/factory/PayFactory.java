package open.api.strategy.factory;

/**
 * ClassName: Factory
 * Package: com.qs.strategy
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/8/3 - 12:50
 * @Version: v1.0
 */

import open.api.strategy.StrategyFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略工厂类
 */
public class PayFactory {
    private static final Map<String,StrategyFactory> STRATEGY_MAP = new ConcurrentHashMap<>();

    /**
     * 根据支付类型获取对应的策略
     * @param payType 支付类型
     * @return 策略对象
     */
    public static StrategyFactory getInvokeStrategy(String payType){
        return STRATEGY_MAP.get(payType);
    }

    /**
     * 注册支付策略
     * @param payType 支付类型
     * @param strategy 策略对象
     */
    public static void register(String payType, StrategyFactory strategy){
        if(payType == null && strategy == null){
            return;
        }
        STRATEGY_MAP.put(payType,strategy);
    }
}
