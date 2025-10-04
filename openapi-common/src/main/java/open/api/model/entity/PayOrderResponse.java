package open.api.model.entity;

/**
 * ClassName: PayOrderResponse
 * Package: com.qs.pojo.entity
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/1/1 - 15:11
 * @Version: v1.0
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付请求响应参数封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderResponse implements Serializable {
    private int code;
    private String msg;
    private String money;
    /**
     * 支付方式：alipay:支付宝,qqpay:QQ钱包,wxpay:微信支付
     */
    private String type;
    /**
     * 如果返回该字段，则根据该url生成二维码
     */
    private String qrcode;
    /**
     * 如果返回该字段，可直接使用该二维码
     */
    private String code_url;
    /**
     * 支付订单号
     */
    private String trade_no;
    /**
     * 商户订单号
     */
    private String out_trade_no;
}
