package open.api.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
@TableName("application_key")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="应用key实体类", description="应用与key联表")
public class ApplicationKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "应用id")
    private Integer applicationId;

    @ApiModelProperty(value = "key名称")
    private String keyName;

    private String accessKey;

    private String secretKey;

    @ApiModelProperty(value = "key的额度：默认10")
    private Long line;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "应用key创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "应用key菜单修改时间")
    private LocalDateTime updateTime;
}
