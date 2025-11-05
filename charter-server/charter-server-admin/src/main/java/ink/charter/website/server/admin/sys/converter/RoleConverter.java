package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.common.core.entity.sys.SysRoleEntity;
import ink.charter.website.domain.admin.api.dto.role.CreateRoleDTO;
import ink.charter.website.domain.admin.api.dto.role.UpdateRoleDTO;
import ink.charter.website.domain.admin.api.vo.role.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 角色转换器
 *
 * @author charter
 * @create 2025/11/05
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {

    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    /**
     * CreateRoleDTO转换为SysRoleEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    SysRoleEntity toEntity(CreateRoleDTO dto);

    /**
     * UpdateRoleDTO转换为SysRoleEntity
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    SysRoleEntity toEntity(UpdateRoleDTO dto);

    /**
     * SysRoleEntity转换为RoleVO
     */
    RoleVO toVO(SysRoleEntity entity);

    /**
     * SysRoleEntity批量转换为RoleVO
     */
    List<RoleVO> toVOList(List<SysRoleEntity> entities);
}
