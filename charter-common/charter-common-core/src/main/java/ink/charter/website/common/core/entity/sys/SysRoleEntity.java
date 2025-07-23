package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统角色实体类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@Schema(name = "SysRoleEntity", description = "系统角色")
public class SysRoleEntity extends BaseEntity {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;

    @Schema(description = "排序")
    private Integer sortOrder;
}