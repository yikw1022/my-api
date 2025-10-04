package open.api.constant;

/**
 * ClassName: Redis
 * Package: open.api.constant
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/5/8 - 0:31
 * @Version: v1.0
 */
public class RedisConstant {

    /**
     * token缓存15分钟
     */
    public static final long TOKEN_CACHE_EXPIRED = 30L;

    /**
     * token缓存key
     */
    public static final String TOKEN_CACHE_KEY = "token:cache:key";

    /**
     * 邮箱验证码缓存3分钟
     */
    public static final long MAIL_CODE_CACHE_EXPIRED = 3L;

    /**
     * token缓存key
     */
    public static final String MAIL_CODE_CACHE_KEY = "mail:cache:key";
}
