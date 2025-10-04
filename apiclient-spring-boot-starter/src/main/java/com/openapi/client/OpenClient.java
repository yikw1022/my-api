package com.openapi.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.openapi.utils.EncodeUtil;
import com.openapi.utils.SignUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * ClassName: OpenClient
 * Package: open.api.client
 * Description:
 */
public class OpenClient {

    /**
     * 添加密钥
     */
    private String secretKey;

    /**
     * 应用key
     */
    private String accessKey;

    /**
     * 服务接口基地址
     *
     */
    private final  String BASE_ULR = "http://localhost:9000/provider";

    /**
     * SDK客户端构造器
     * @param accessKey 应用key
     * @param secretKey 密钥
     */
    public OpenClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 查询ICP备案信息
     * @param domainName 域名
     * @return 结果
     */
    public String queryICPInfo(String domainName){
        HashMap<String, Object> param = new HashMap<>();
        param.put("domainName", domainName);
        param.put("url",BASE_ULR + "/icp/info");
        String json = JSONUtil.toJsonStr(param);
        return HttpRequest.get(BASE_ULR + "/icp/info")
                .addHeaders(getHeader(json))
                .form(param)
                .execute()
                .body();
    }

    /**
     * IP信息获取
     * @param ip ip地址
     * @return 结果
     */
    public String queryIpInfo(String ip){
        HashMap<String, Object> param = new HashMap<>();
        param.put("ip", ip);
        param.put("url",BASE_ULR + "/query/ipInfo");
        String json = JSONUtil.toJsonStr(param);
        return HttpRequest.post(BASE_ULR + "/query/ipInfo")
                .addHeaders(getHeader(json))
                .form(param)
                .execute()
                .body();
    }

    /**
     * url地址转二维码
     * @param urlValue url地址
     * @return 结果
     */
    public String queryUrlQrCode(String urlValue){
        HashMap<String, Object> param = new HashMap<>();
        param.put("urlValue", urlValue);
        param.put("url",BASE_ULR + "/query/url/qrcode");
        String json = JSONUtil.toJsonStr(param);
        return HttpRequest.post(BASE_ULR + "/query/url/qrcode")
                .addHeaders(getHeader(json))
                .form(param)
                .execute()
                .body();
    }

    /**
     * QQ信息获取
     * @param qq qq号
     * @return 结果
     */
    public String queryQqInfo(String qq){
        HashMap<String, Object> param = new HashMap<>();
        param.put("qq", qq);
        param.put("url",BASE_ULR + "/query/qqInfo");
        String json = JSONUtil.toJsonStr(param);
        return HttpRequest.post(BASE_ULR + "/query/qqInfo")
                .addHeaders(getHeader(json))
                .form(param)
                .execute()
                .body();
    }

    /**
     * 城市天气查询
     * @param city 城市名称
     * @return 结果
     */
    public String queryCityWeatherInfo(String city){
        HashMap<String, Object> param = new HashMap<>();
        param.put("city", city);
        param.put("url",BASE_ULR + "/query/city/weather");
        String json = JSONUtil.toJsonStr(param);
        return HttpRequest.post(BASE_ULR + "/query/city/weather")
                .addHeaders(getHeader(json))
                .form(param)
                .execute()
                .body();
    }

    /**
     * 字符数量检测
     * @param text 要查询的文本
     * @return 结果
     */
    public String charQuantityDetection(String text){
        HashMap<String, Object> param = new HashMap<>();
        param.put("text", text);
        param.put("url",BASE_ULR + "/detection/char/count");
        String json = JSONUtil.toJsonStr(param);
        return HttpRequest.post(BASE_ULR + "/detection/char/count")
                .addHeaders(getHeader(json))
                .form(param)
                .execute()
                .body();
    }

    /**
     * 敏感词检测
     * @param text 要检测的文本
     * @return 结果
     */
    public String sensitiveWordsDetection(String text){
        HashMap<String, Object> param = new HashMap<>();
        param.put("text", text);
        param.put("url",BASE_ULR + "/detection/sensitive/words");
        String json = JSONUtil.toJsonStr(param);
        return HttpRequest.post(BASE_ULR + "/detection/sensitive/words")
                .addHeaders(getHeader(json))
                .form(param)
                .execute()
                .body();
    }

    /**
     * 中英互译
     * @param text 要翻译的文本
     * @return 结果
     */
    public String chineseEnglishTranslation(String text){
        HashMap<String, Object> param = new HashMap<>();
        param.put("text", text);
        param.put("url",BASE_ULR + "/chinese/translation/to/english");
        String json = JSONUtil.toJsonStr(param);
        return HttpRequest.post(BASE_ULR + "/chinese/translation/to/english")
                .addHeaders(getHeader(json))
                .form(param)
                .execute()
                .body();
    }

    /**
     * 封装header参数
     * @param body 参数体
     * @return 参数封装
     */
    private Map<String, String> getHeader(String body){
        Map<String, String> header = new HashMap<>();
        header.put("accessKey", this.accessKey);
        //将不在服务器间传递
        //header.put("secretKey", this.secretKey);
        //解决中文乱码问题
        String encodeBody = EncodeUtil.encodeBody(body);
        header.put("body",encodeBody);
        header.put("sign", SignUtil.getSign(encodeBody,secretKey));
        //随机数 + 时间戳的组合实现将在SDK封装时实现。
        header.put("nonce", RandomUtil.randomNumbers(4));
        header.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        return header;
    }
}
