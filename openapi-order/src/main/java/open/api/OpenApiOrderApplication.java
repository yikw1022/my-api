package open.api;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
@Slf4j
@SpringBootApplication
@MapperScan("open.api.mapper")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients
public class OpenApiOrderApplication
{
    public static void main( String[] args ) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(OpenApiOrderApplication.class, args);
        log.info("==============订单管理模块==============\n");
        //获取配置信息
        ConfigurableEnvironment environment = application.getEnvironment();
        //获取port
        String port = environment.getProperty("server.port");
        //获取ip主机
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println("\n----------------------------------------------------------\n\t" +
                "OpenAPI开放平台项目启动成功! Access URLs:\n\t" +
                "Knife4j-ui: \thttp://" + ip + ":" + port + "/doc.html\n\t" +
                "----------------------------------------------------------");
    }
}
