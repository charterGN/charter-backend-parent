package ink.charter.website.domain.admin.api.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * 用户权限信息VO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Schema(description = "用户权限信息")
public class UserPermissionsVO {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "角色列表", example = "[\"admin\", \"user\"]")
    private Set<String> roles;

    @Schema(description = "权限列表", example = "[\"user:read\", \"user:write\", \"system:admin\"]")
    private Set<String> permissions;

    /**
     * 静态工厂方法
     */
    public static UserPermissionsVO of(Long userId, String username, Set<String> roles, Set<String> permissions) {
        UserPermissionsVO vo = new UserPermissionsVO();
        vo.setUserId(userId);
        vo.setUsername(username);
        vo.setRoles(roles);
        vo.setPermissions(permissions);
        return vo;
    }
}