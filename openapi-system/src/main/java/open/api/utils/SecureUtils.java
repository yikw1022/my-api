package open.api.utils;

/**
 * ClassName: SecureUtils
 * Package: open.api.utils
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/4/27 - 19:04
 * @Version: v1.0
 */

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 生成accessKey和secretKey
 */
public class SecureUtils {

    /**
     * 生成accessKey
     * @param keyName
     * @return
     */
    public static String generateAccessKey(String keyName){
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String content = keyName + "." + RandomUtil.randomNumbers(5);
        return md5.digestHex(content);
    }

    /**
     * 生成secretKey
     * @param keyName
     * @return
     */
    public static String generateSecretKey(String keyName){
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String content = keyName + "." + RandomUtil.randomNumbers(10);
        return md5.digestHex(content);
    }
}
