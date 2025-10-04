package open.api.model.enums;

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
 * 菜单类型：0：全部订单；1：已支付 ；2：未支付
 */
@Getter
public enum QueryOrderEnum {
    All_DATA(0,"全部订单"),
    HAVEDPAY_DATA(1,"已支付"),
    UNPAY_DATA(2,"未支付");
    /**
     * 响应码
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String type;

    /**
     * 如何定义一个通用的枚举类
     * 举值-->构造-->遍历
     */

    /**
     * 构造
     * @param code
     * @param type
     * @return
     */
    QueryOrderEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    /**
     * 遍历
     * @param code
     * @return
     */
    public static QueryOrderEnum getResponseCodeEnum(int code) {
        for (QueryOrderEnum menuTypeEnum : QueryOrderEnum.values()) {
            if (menuTypeEnum.getCode() == code) {
                return menuTypeEnum;
            }
        }
        return null;
    }
}
