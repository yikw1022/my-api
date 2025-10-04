package open.api.utils;

/**
 * ClassName: RestTemplateUtil
 * Package: com.qs.utils
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2023/12/31 - 10:56
 * @Version: v1.0
 */

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * post和get请求封装
 */
public class RestTemplateUtil {
    /**
     * post请求
     *
     * @param url       请求路径
     * @param parameter 请求参数
     * @return 返回值
     */
    public static String post(String url, Map<String, String> parameter) {
        RestTemplate restTemplate = new RestTemplate();
        /**
         * 防止乱码
         */
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> param = new HttpEntity<Map<String, String>>(parameter, requestHeaders);
        String body = restTemplate.postForObject(url, param, String.class);
        return body;
    }

    /**
     * get请求
     *
     * @param url       请求路径
     * @param parameter 请求参数
     * @return 返回值
     */
    public static String get(String url, Map<String, Object> parameter) {
        if (!parameter.isEmpty()) {
            url = url + "?";
            for (String key : parameter.keySet()) {
                url = url + key + "=" + parameter.get(key) + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String body = restTemplate.getForEntity(url, String.class, parameter).getBody();
        return body;
    }
}
