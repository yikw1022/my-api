package com.openapi;

import com.openapi.client.OpenClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: OpenApiClientConfig
 * Package: open.api
 * Description:
 */
@Data
@Configuration
@ConfigurationProperties("open.api")
@ComponentScan
public class OpenApiClientConfig {

    /**
     * key
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 初始化SDK客户端
     * @return SDK客户端
     */
    @Bean
    public OpenClient openClient() {
        return new OpenClient(accessKey, secretKey);
    }
}
