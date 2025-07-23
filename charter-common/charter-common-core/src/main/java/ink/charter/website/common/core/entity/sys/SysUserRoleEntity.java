package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户角色关联实体类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_role")
@Schema(name = "SysUserRoleEntity", description = "用户角色关联")
public class SysUserRoleEntity extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色ID")
    private Long roleId;
}