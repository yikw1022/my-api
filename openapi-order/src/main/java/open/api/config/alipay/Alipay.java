package open.api.config.alipay;

import com.alipay.api.AlipayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: @weixueshi
 * @Create: 2024/6/18 - 15:04
 * @Version: v1.0
 */

/**
 * 支付宝配置类
 */
@Configuration
public class Alipay {

    @Value("${alipay.serverUrl}")
    private String serverUrl;

    @Value("${alipay.appId}")
    private String appId;

    @Value("${alipay.privateKey}")
    private String privateKey;

    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;


    @Bean
    public AlipayConfig alipayConfig(){
        AlipayConfig config = new AlipayConfig();
        config.setServerUrl(serverUrl);
        config.setAppId(appId);
        config.setPrivateKey(privateKey);
        config.setFormat("json");
        config.setAlipayPublicKey(alipayPublicKey);
        config.setCharset("UTF-8");
        config.setSignType("RSA2");
        return config;
    }
}
