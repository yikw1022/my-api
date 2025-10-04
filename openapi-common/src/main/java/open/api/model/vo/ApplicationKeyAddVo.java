package open.api.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 应用与key联表
 * </p>
 *
 * @author author
 * @since 2024-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationKeyAddVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "应用id")
    private Integer applicationId;

    @ApiModelProperty(value = "key名称")
    private String keyName;
}
