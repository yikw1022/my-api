package open.api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ClassName: PayResponse
 * Package: com.qs.pojo.entity
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/1/1 - 17:05
 * @Version: v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String payUrl;
    private String tradeNo;
}
