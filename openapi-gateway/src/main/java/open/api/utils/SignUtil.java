package open.api.utils;

/**
 * ClassName: SignUtil
 * Package: open.api.utils
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/4/27 - 0:08
 * @Version: v1.0
 */

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 生成签名参数
 */
public class SignUtil {
    /**
     * 生成签名参数
     * @param body
     * @return
     */
    public static String getSign(String body,String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        //参数主要有一个盐值(增加安全系数) + 用户请求参数 + 密钥；
        String content = body.toString() + "." + secretKey;
        return md5.digestHex(content);
    }
}
