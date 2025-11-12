package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统资源实体类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_resource")
@Schema(name = "SysResourceEntity", description = "系统资源")
public class SysResourceEntity extends BaseEntity {

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "资源编码")
    private String resourceCode;

    @Schema(description = "所属模块")
    private String module;

    @Schema(description = "资源URL")
    private String url;

    @Schema(description = "HTTP方法")
    private String method;

    @Schema(description = "资源描述")
    private String description;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;
}