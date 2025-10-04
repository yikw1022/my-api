package open.api.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 应用管理表
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
public class ApplicationListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "应用名称")
    private String name;

    @ApiModelProperty(value = "应用状态：0：禁用；1：开启")
    private boolean status;

    @ApiModelProperty(value = "应用创建时间")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "应用修改时间")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
