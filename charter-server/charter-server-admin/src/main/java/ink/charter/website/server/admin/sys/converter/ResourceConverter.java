package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.common.core.entity.sys.SysResourceEntity;
import ink.charter.website.domain.admin.api.dto.resource.CreateResourceDTO;
import ink.charter.website.domain.admin.api.dto.resource.UpdateResourceDTO;
import ink.charter.website.domain.admin.api.vo.resource.ResourceVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 资源转换器
 *
 * @author charter
 * @create 2025/11/05
 */
@Mapper(componentModel = "spring")
public interface ResourceConverter {

    ResourceConverter INSTANCE = Mappers.getMapper(ResourceConverter.class);

    /**
     * CreateResourceDTO转换为SysResourceEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    SysResourceEntity toEntity(CreateResourceDTO dto);

    /**
     * UpdateResourceDTO转换为SysResourceEntity
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    SysResourceEntity toEntity(UpdateResourceDTO dto);

    /**
     * SysResourceEntity转换为ResourceVO
     */
    ResourceVO toVO(SysResourceEntity entity);

    /**
     * SysResourceEntity批量转换为ResourceVO
     */
    List<ResourceVO> toVOList(List<SysResourceEntity> entities);
}
