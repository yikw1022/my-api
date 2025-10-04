package open.api.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTHeader;
import cn.hutool.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * ClassName: JwtUtil
 * Package: open.api.utils
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/5/7 - 23:46
 * @Version: v1.0
 */
public class JwtUtil {

    /**
     * 盐值
     */
    private static final String KEY = "openapi";

    /**
     * 根据用户id创建token
     * @param userId
     * @return
     */
    public static String createToken(Integer userId){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
        return JWTUtil.createToken(map, KEY.getBytes());
    }

    /**
     * 解析token获取用户id
     * @param token
     * @return
     */
    public static Integer parseToken(String token){
        final JWT jwt = JWTUtil.parseToken(token);
        JSONObject payloads = jwt.getPayloads();
        String userId = payloads.getStr("userId");
        return Integer.parseInt(userId);
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public static boolean verifyToken(String token,Integer userId){
        return JWTUtil.verify(token, String.valueOf(userId).getBytes());
    }
}
