package open.api.response;

/**
 * ClassName: ResponseCodeEnum
 * Package: org.common.response
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/3/10 - 11:01
 * @Version: v1.0
 */

import lombok.Getter;

/**
 * 统一响应枚举值
 */
@Getter
public enum ResponseCodeEnum {
    FAIL(403,"登录过期，请重新登录！"),
    SUCCESS(200,"请求成功"),
    NEED_LOGIN(402,"需要登录后操作"),
    SERVER_ERROR(500,"服务器错误"),

    /**
     * 业务码
     */
    ADMIN_NORMAL(1001,"状态正常，无法删除！"),
    USER_NOT_EXITS(1002,"用户不存在！"),
    PHONE_ERROR(1003,"手机号错误！"),
    PASSWOR_ERROR(1004,"密码错误！"),
    MENU_EXITS(1005,"菜单目录已存在！"),
    PARENT_DIRECTOR_NOT_EXITS(1006,"父级目录不存在！"),
    PARENT_MENU_NOT_EXITS(1006,"父级菜单不存在！"),
    MENU_HAS_SON(1007,"包含子菜单，无法删除！"),
    VERTIFY_CODE_ERROR(1008,"认证失败！"),
    APP_NAME_EXITS(1009,"应用名称已存在！"),
    KEY_EXCEED(1010,"每个应用最多添加十个key！"),
    URL_EXITS(1011,"接口请求URL不能重复！"),
    INTERFACE_NOT_EXITS(1012,"接口不存在！"),
    INTERFACE_NOT_PASS(1013,"接口测试不通过！"),
    UNSUPORT_PARAM_TYPE(1014,"不支持的参数类型！"),
    INTRFACE_OFFLINE(1015,"接口尚未发布，无法调用！"),
    ACCESSKEY_ERROR(1016,"accesskey错误！"),
    IPADDRESS_FORBIDDEN(1017,"请求IP不在设置的白名单中！"),
    APPLICATION_FORBIDDEN(1018,"接口调用失败，请开启应用！"),
    LIINES_INSUFFICIENT(1019,"额度不足，请充值！"),
    PEY_TYPE_UNSUPORT(1020,"不支持的支付方式！"),
    SYSTEM_ERROR(1021,"系统错误！"),
    KEY_NAME_EXITS(1022,"key名称已存在！"),
    USERNAME_EXITS(1023,"用户名已存在！"),
    PHONE_EXITS(1024,"手机号已存在！"),
    EMAIL_EXITS(1025,"邮箱已存在！"),
    USER_FORBIDDEN(1026,"用户已被禁用，请联系管理员恢复！"),
    INTERFACE_ERROR(1027,"接口调用失败！"),
    CODE_EXPIRED(1028,"验证码已过期，请重新获取！"),
    CODE_ERROR(1029,"验证码错误！"),
    EMAIL_UNREGISTER(1030,"邮箱未注册！");

    /**
     * 响应码
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String message;

    /**
     * 如何定义一个通用的枚举类
     * 举值-->构造-->遍历
     */

    /**
     * 构造
     * @param code
     * @param message
     * @return
     */
    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 遍历
     * @param code
     * @return
     */
    public static  ResponseCodeEnum getResponseCodeEnum(int code) {
        for (ResponseCodeEnum responseCodeEnum : ResponseCodeEnum.values()) {
            if (responseCodeEnum.getCode() == code) {
                return responseCodeEnum;
            }
        }
        return null;
    }
}
