package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.server.admin.sys.dto.dict.CreateDictDataDTO;
import ink.charter.website.server.admin.sys.dto.dict.UpdateDictDataDTO;
import ink.charter.website.server.admin.sys.vo.dict.DictDataVO;
import ink.charter.website.common.core.entity.sys.SysDictDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 字典数据转换器
 *
 * @author charter
 * @create 2025/09/15
 */
@Mapper(componentModel = "spring")
public interface DictDataConverter {

    DictDataConverter INSTANCE = Mappers.getMapper(DictDataConverter.class);

    /**
     * CreateDictDataDTO转换为SysDictDataEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    SysDictDataEntity toEntity(CreateDictDataDTO dto);

    /**
     * UpdateDictDataDTO转换为SysDictDataEntity
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    SysDictDataEntity toEntity(UpdateDictDataDTO dto);

    /**
     * SysDictDataEntity转换为DictDataVO
     */
    DictDataVO toVO(SysDictDataEntity entity);
}