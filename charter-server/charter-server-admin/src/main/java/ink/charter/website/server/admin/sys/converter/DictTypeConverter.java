package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.server.admin.sys.dto.dict.CreateDictTypeDTO;
import ink.charter.website.server.admin.sys.dto.dict.UpdateDictTypeDTO;
import ink.charter.website.server.admin.sys.vo.dict.DictTypeVO;
import ink.charter.website.common.core.entity.sys.SysDictTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 字典类型转换器
 *
 * @author charter
 * @create 2025/09/15
 */
@Mapper(componentModel = "spring")
public interface DictTypeConverter {

    DictTypeConverter INSTANCE = Mappers.getMapper(DictTypeConverter.class);

    /**
     * CreateDictTypeDTO转换为SysDictTypeEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    SysDictTypeEntity toEntity(CreateDictTypeDTO dto);

    /**
     * UpdateDictTypeDTO转换为SysDictTypeEntity
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    SysDictTypeEntity toEntity(UpdateDictTypeDTO dto);

    /**
     * SysDictTypeEntity转换为DictTypeVO
     */
    DictTypeVO toVO(SysDictTypeEntity entity);
}