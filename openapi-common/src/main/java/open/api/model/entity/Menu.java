package open.api.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2024-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("menu")
@ApiModel(value = "菜单实体类", description = "菜单")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "父节点ID")
    private Integer parentId;

    @ApiModelProperty(value = "菜单路径")
    private String path;

    @ApiModelProperty(value = "按钮权限")
    private String button;

    @ApiModelProperty(value = "菜单标题")
    private String title;

    @ApiModelProperty(value = "菜单类型：0：目录;1：菜单;2：按钮")
    private Integer type;

    @ApiModelProperty(value = "菜单序号")
    private Integer sort;

    @ApiModelProperty(value = "菜单状态：0：不可用; 1：可以")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "菜单创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "菜单修改时间")
    private LocalDateTime updateTime;
}
