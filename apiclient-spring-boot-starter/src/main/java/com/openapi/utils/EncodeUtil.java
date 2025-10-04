package com.openapi.utils;

/**
 * ClassName: EncodeUtil
 * Package: open.api.utils
 * Description:
 */

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 加解密工具
 */
public class EncodeUtil {

    /**
     * 对用户参数body进行编码
     * @param body 请求体
     * @return 加密体
     */
    public static String encodeBody(String body){
        //解决参数中文乱码
        String encode ="";
        try {
            encode = URLEncoder.encode(body, "utf-8");
            return encode;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对用户参数body进行解码
     * @param body 请求体
     * @return 解密体
     */
    public static String decodeBody(String body){
        try {
            body = URLDecoder.decode(body,"utf-8");
            return body;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
