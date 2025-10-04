package open.api.config;

/**
 * ClassName: BlockingLoadBalancerClientConfig
 * Package: open.api.config
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/5/1 - 11:45
 * @Version: v1.0
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class BlockingLoadBalancerClientConfig {

    @Autowired
    LoadBalancerClientFactory loadBalancerClientFactory;

    @Autowired
    LoadBalancerProperties properties;

    @Bean
    public LoadBalancerClient BlockingLoadBalancerClient() {
        return new CustomBlockingLoadBalancerClient(loadBalancerClientFactory, properties);
    }
}

