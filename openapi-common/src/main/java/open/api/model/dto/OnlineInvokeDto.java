package open.api.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

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
public class OnlineInvokeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口id")
    private Integer id;

    @ApiModelProperty(value = "接口地址")
    @NotBlank(message = "接口地址不能为空")
    private String url;

    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    @ApiModelProperty(value = "接口请求参数")
    private String params;
}
