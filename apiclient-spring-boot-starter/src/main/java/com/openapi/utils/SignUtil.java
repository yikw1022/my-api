package com.openapi.utils;

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
     * @param body 参数体
     * @param secretKey 应用密钥
     * @return 签名数据
     */
    public static String getSign(String body,String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String content = body.toString() + "." + secretKey;
        return md5.digestHex(content);
    }
}
