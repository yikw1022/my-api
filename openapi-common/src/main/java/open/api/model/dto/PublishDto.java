package open.api.model.dto;

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
public class PublishDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口id")
    private Integer id;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "接口请求参数")
    private String params;
}
