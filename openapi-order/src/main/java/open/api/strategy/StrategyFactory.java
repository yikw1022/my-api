package open.api.strategy;

/**
 * ClassName: StrategyFactory
 * Package: com.qs.strategy
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/8/3 - 12:48
 * @Version: v1.0
 */

import open.api.exception.SystemException;
import open.api.model.entity.Pay;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * 策略工厂：为什么要实现InitializingBean呢？
 * 这是Spring提供的方法，将所有bean加载完成后有一个
 * 方法void afterPropertiesSet() throws Exception;
 * 可以在这个方法中实现策略对象注册
 */
public interface StrategyFactory extends InitializingBean {
    /**
     * 获取支付策略
     * @param pay 支付类实体
     * @return 支付策略
     */
    Map<String,Object> pay(Pay pay) throws SystemException;
}
