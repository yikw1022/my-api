package open.api.model.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 额度套餐表
 * </p>
 *
 * @author author
 * @since 2024-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("plan")
@ApiModel(value="Plan对象", description="额度套餐表")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "套餐名称")
    @NotBlank(message = "套餐名称不能为空")
    private String name;

    @ApiModelProperty(value = "套餐价格")
    @NotNull(message = "套餐价格不能为空")
    private BigDecimal price;

    @ApiModelProperty(value = "套餐单位")
    @NotBlank(message = "套餐单位不能为空")
    private String unit;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "套餐创建时间")
    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "套餐修改时间")
    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private LocalDateTime updateTime;
}
