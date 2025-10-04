package open.api.utils;

/**
 * ClassName: EncodeUtil
 * Package: open.api.utils
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/4/27 - 0:30
 * @Version: v1.0
 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 针对hutool post请求中文乱码解决 的编码工具类
 */
public class EncodeUtil {

    /**
     * 对用户参数body进行编码
     * @param body
     * @return
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
     * @param body
     * @return
     */
    public static String decodeBody(String body){
        //解码，解决中文乱码问题
        try {
            body = URLDecoder.decode(body,"utf-8");
            return body;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
