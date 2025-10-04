package open.api.model.entity;

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

/**
 * <p>
 * ip白名单表
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ip_white")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="IpWhite对象", description="ip白名单表")
public class IpWhite implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer keyId;

    private String keyName;

    @ApiModelProperty(value = "IP地址")
    private String address;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "白名单创建时间")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "白名单修改时间")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
