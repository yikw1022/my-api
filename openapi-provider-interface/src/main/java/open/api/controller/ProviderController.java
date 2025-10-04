package open.api.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.openapi.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import open.api.exception.SystemException;
import open.api.response.ResponseCodeEnum;
import open.api.response.ResultData;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ProviderController
 * Package: open.api.controller.provider
 * Description: 对外提供接口供开发者调用
 *
 * @Author: @weixueshi
 * @Create: 2024/4/26 - 20:09
 * @Version: v1.0
 */
@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/provider")
public class ProviderController {

    /**
     * 查询ICP备案信息
     */
    @GetMapping("/icp/info")
    public ResultData queryICPInfo(@RequestParam("domainName") String domainName) {
        log.info("查询ICP备案信息，参数：{}", domainName);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("domain", domainName);
        String result = HttpUtil.get("https://uapis.cn/api/icp", param);
        String code = (String) JSONUtil.parseObj(result).get("code");
        if("200".equalsIgnoreCase(code)) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("unitName", JSON.parseObject(result).get("unitName"));
            map.put("domain",JSON.parseObject(result).get("domain"));
            map.put("mainLicence",JSON.parseObject(result).get("mainLicence"));
            map.put("serviceLicence",JSON.parseObject(result).get("serviceLicence"));
            map.put("natureName",JSON.parseObject(result).get("natureName"));
            map.put("updateRecordTime",JSON.parseObject(result).get("updateRecordTime"));
            return ResultData.success(map);
        }
        return ResultData.success((String) JSON.parseObject(result).get("msg"));
    }

    /**
     * IP信息获取
     */
    @PostMapping("/query/ipInfo")
    public ResultData queryIpInfo(@RequestParam("ip") String ip) {
        log.info("IP信息获取，IP参数：{}", ip);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("ip", ip);
        String result = HttpUtil.get("https://uapis.cn/api/ipinfo", param);
        Integer code = (Integer) JSONUtil.parseObj(result).get("code");
        if(200 == code) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("ip", JSON.parseObject(result).get("ip"));
            map.put("country",JSON.parseObject(result).get("country"));
            map.put("region",JSON.parseObject(result).get("region"));
            map.put("city",JSON.parseObject(result).get("city"));
            map.put("asn",JSON.parseObject(result).get("asn"));
            map.put("latitude",JSON.parseObject(result).get("latitude"));
            map.put("longitude",JSON.parseObject(result).get("longitude"));
            return ResultData.success(map);
        }
        return ResultData.success((String) JSON.parseObject(result).get("msg"));
    }

    /**
     * url地址转二维码
     */
    @PostMapping("/query/url/qrcode")
    public ResultData queryUrlQrCode(@RequestParam("urlValue") String urlValue) {
        log.info("url地址转二维码，url参数：{}", urlValue);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("text", urlValue);
        String result = HttpUtil.get("https://uapis.cn/api/textqrcode", param);
        Integer code = (Integer) JSONUtil.parseObj(result).get("code");
        if(200 == code) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("text", JSON.parseObject(result).get("text"));
            map.put("qrcode",JSON.parseObject(result).get("qrcode"));
            return ResultData.success(map);
        }
        return ResultData.success((String) JSON.parseObject(result).get("error"));
    }

    /**
     * QQ信息获取
     */
    @PostMapping("/query/qqInfo")
    public ResultData queryQqInfo(@RequestParam("qq") String qq) {
        log.info("QQ信息获取，QQ号参数：{}", qq);
        Map<String,Object> param = new HashMap<String,Object>(2);
        param.put("pskey", "EkI5gCcA36f9l6TERWtkBsZ6-TZ1K**jdl9*ZEJ1U7w_");
        param.put("qq", qq);
        param.put("uin", "2073958890");
        String result = HttpUtil.get("https://api.ilingku.com/int/v1/qqinfo", param);
        Integer code = (Integer) JSONUtil.parseObj(result).get("code");
        if(200 == code) {
            String data = JSONUtil.parseObj(result).get("data").toString();
            Map<String,Object> map = JSONUtil.toBean(data, Map.class);
            return ResultData.success(map);
        }
        return ResultData.success((String) JSON.parseObject(result).get("msg"));
    }

    /**
     * 城市天气查询
     */
    @PostMapping("/query/city/weather")
    public ResultData queryCityWeatherInfo(@RequestParam("city") String city) {
        log.info("城市天气查询，城市参数：{}", city);
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("name", city);
        String result = HttpUtil.get("https://uapis.cn/api/weather", param);
        Integer code = (Integer) JSONUtil.parseObj(result).get("code");
        if(200 == code) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("province", JSON.parseObject(result).get("province"));
            map.put("city",JSON.parseObject(result).get("city"));
            map.put("temperature",JSON.parseObject(result).get("temperature"));
            map.put("weather",JSON.parseObject(result).get("weather"));
            map.put("wind_direction",JSON.parseObject(result).get("wind_direction"));
            map.put("wind_power",JSON.parseObject(result).get("wind_power"));
            map.put("humidity",JSON.parseObject(result).get("humidity"));
            map.put("reporttime",JSON.parseObject(result).get("reporttime"));
            return ResultData.success(map);
        }
        return ResultData.success((String) JSON.parseObject(result).get("msg"));
    }

    /**
     * 字符数量检测
     */
    @PostMapping("/detection/char/count")
    public ResultData charQuantityDetection(@RequestParam("text") String text) {
        log.info("字符数量检测");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("text", text);
        String result = HttpUtil.get("https://uapis.cn/api/texts", param);
        Integer code = (Integer) JSONUtil.parseObj(result).get("code");
        if(200 == code) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("text", JSON.parseObject(result).get("text"));
            map.put("chinese",JSON.parseObject(result).get("chinese"));
            map.put("punctuation",JSON.parseObject(result).get("punctuation"));
            map.put("letters",JSON.parseObject(result).get("letters"));
            map.put("characters",JSON.parseObject(result).get("characters"));
            return ResultData.success(map);
        }
        return ResultData.success((String) JSON.parseObject(result).get("msg"));
    }

    /**
     * 敏感词检测
     */
    @PostMapping("/detection/sensitive/words")
    public ResultData sensitiveWordsDetection(@RequestParam("text") String text) {
        log.info("敏感词检测");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("text", text);
        String result = HttpUtil.get("https://uapis.cn/api/prohibited", param);
        Integer code = (Integer) JSONUtil.parseObj(result).get("code");
        if(200 == code) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("status", JSON.parseObject(result).get("status"));
            map.put("forbiddenWord",JSON.parseObject(result).get("forbiddenWord"));
            map.put("text",JSON.parseObject(result).get("text"));
            return ResultData.success(map);
        }
        return ResultData.success((String) JSON.parseObject(result).get("msg"));
    }

    /**
     * 中英互译
     */
    @PostMapping("/chinese/translation/to/english")
    public ResultData chineseEnglishTranslation(@RequestParam("text") String text) {
        log.info("中英互译");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("text", text);
        String result = HttpUtil.get("https://uapis.cn/api/fanyi", param);
        Integer code = (Integer) JSONUtil.parseObj(result).get("code");
        if(200 == code) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("text", JSON.parseObject(result).get("text"));
            map.put("translate",JSON.parseObject(result).get("translate"));
            return ResultData.success(map);
        }
        return ResultData.success((String) JSON.parseObject(result).get("msg"));
    }
}
