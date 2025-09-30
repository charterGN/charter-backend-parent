package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.domain.admin.api.dto.user.CreateUserDTO;
import ink.charter.website.domain.admin.api.dto.user.UpdateUserDTO;
import ink.charter.website.domain.admin.api.vo.user.UserPermissionsVO;
import ink.charter.website.domain.admin.api.vo.user.UserVO;
import ink.charter.website.common.core.entity.sys.SysUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

/**
 * 用户转换器
 *
 * @author charter
 * @create 2025/07/17
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * CreateUserDTO转换为SysUserEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salt", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "loginCount", ignore = true)
    SysUserEntity toEntity(CreateUserDTO dto);

    /**
     * UpdateUserDTO转换为SysUserEntity
     */
    @Mapping(target = "id", source = "userId")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "salt", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "loginCount", ignore = true)
    SysUserEntity toEntity(UpdateUserDTO dto);

    /**
     * SysUserEntity转换为UserVO
     */
    @Mapping(target = "userId", source = "id")
    UserVO toVO(SysUserEntity entity);

    /**
     * SysUserEntity批量转换为UserVO
     */
    @Mapping(target = "userId", source = "id")
    List<UserVO> toVOList(List<SysUserEntity> entities);

    /**
     * 转换为用户权限VO
     */
    default UserPermissionsVO toPermissionsVO(SysUserEntity user, Set<String>  roles, Set<String> permissions) {
        if (user == null) {
            return null;
        }
        
        return UserPermissionsVO.of(user.getId(), user.getUsername(), roles, permissions);
    }
}