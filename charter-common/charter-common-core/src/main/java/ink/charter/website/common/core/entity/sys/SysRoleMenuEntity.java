package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色菜单关联实体类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_menu")
@Schema(name = "SysRoleMenuEntity", description = "角色菜单关联")
public class SysRoleMenuEntity extends BaseEntity {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单ID")
    private Long menuId;
}