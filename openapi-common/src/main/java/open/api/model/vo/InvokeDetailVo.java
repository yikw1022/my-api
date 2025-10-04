package open.api.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: InvokeDetailVo
 * Package: open.api.model.vo
 * Description:
 *
 * @Author: @weixueshi
 * @Create: 2024/5/8 - 22:05
 * @Version: v1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class InvokeDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyName;

    private List<String> createTime;

    private List<Long> count;
}
