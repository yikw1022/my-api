package open.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.errorprone.annotations.NoAllocation;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * ClassName: Pay
 * Package: open.api.model.entity
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/5/5 - 16:22
 * @Version: v1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "支付实体类", description = "pay")
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品金额
     */
    @NotBlank(message = "商品金额不能为空")
    private String price;

    /**
     * 支付方式
     */
    @NotBlank(message = "支付方式不能为空")
    private String paymentType;
}
