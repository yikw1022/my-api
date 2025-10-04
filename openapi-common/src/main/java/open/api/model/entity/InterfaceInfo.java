package open.api.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 接口信息表
 * </p>
 *
 * @author author
 * @since 2024-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("interface_info")
@ApiModel(value="接口信息实体类", description="接口信息表")
public class InterfaceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "接口创建者id")
    private Integer userId;

    @ApiModelProperty(value = "接口名称")
    private String name;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "接口请求类型：如：post;get;put;delete")
    private String method;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "接口调用次数统计")
    private Long number;

    @ApiModelProperty(value = "接口额度设置")
    private Long Line;

    @ApiModelProperty(value = "接口描述")
    private String description;

    @ApiModelProperty(value = "请求头")
    private String requestHeader;

    @ApiModelProperty(value = "响应头")
    private String responseHeader;

    @ApiModelProperty(value = "接口状态：0：关闭；1：开启")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "菜单创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "菜单修改时间")
    private LocalDateTime updateTime;
}
