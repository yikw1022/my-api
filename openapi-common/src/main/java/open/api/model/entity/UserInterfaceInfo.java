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
 * 用户调用接口次数信息表
 * </p>
 *
 * @author author
 * @since 2024-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_interface_info")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="UserInterfaceInfo对象", description="用户调用接口次数信息表")
public class UserInterfaceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "接口id")
    private Integer interfaceId;

    @ApiModelProperty(value = "应用key名称")
    private String keyName;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "用户-接口信息创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "用户-接口信息修改时间")
    private LocalDateTime updateTime;
}
