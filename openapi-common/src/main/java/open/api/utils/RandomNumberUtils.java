package open.api.utils;

/**
 * ClassName: RandomNumberUtils
 * Package: com.qs.utils
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2023/11/19 - 22:29
 * @Version: v1.0
 */

import cn.hutool.core.util.NumberUtil;

/**
 * 生成6位随机数
 */
public class RandomNumberUtils {


    public static String generateRandomNumber() {
        int[] ints = NumberUtil.generateRandomNumber(1, 9, 6);
        String code = "";
        for (int anInt : ints) {
            code = code + anInt;
        }
        return code;
    }
}
