package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.common.core.entity.sys.SysFileConfigEntity;
import ink.charter.website.domain.admin.api.dto.fileconfig.CreateFileConfigDTO;
import ink.charter.website.domain.admin.api.dto.fileconfig.UpdateFileConfigDTO;
import ink.charter.website.domain.admin.api.vo.fileconfig.FileConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 文件配置转换器
 *
 * @author charter
 * @create 2025/11/14
 */
@Mapper(componentModel = "spring")
public interface FileConfigConverter {

    FileConfigConverter INSTANCE = Mappers.getMapper(FileConfigConverter.class);

    /**
     * CreateFileConfigDTO转换为SysFileConfigEntity
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createTime", ignore = true),
        @Mapping(target = "updateTime", ignore = true),
        @Mapping(target = "isDeleted", ignore = true),
        @Mapping(target = "param", ignore = true)
    })
    SysFileConfigEntity toEntity(CreateFileConfigDTO dto);

    /**
     * UpdateFileConfigDTO转换为SysFileConfigEntity
     */
    @Mappings({
        @Mapping(target = "createTime", ignore = true),
        @Mapping(target = "updateTime", ignore = true),
        @Mapping(target = "isDeleted", ignore = true),
        @Mapping(target = "param", ignore = true)
    })
    SysFileConfigEntity toEntity(UpdateFileConfigDTO dto);

    /**
     * SysFileConfigEntity转换为FileConfigVO
     */
    FileConfigVO toVO(SysFileConfigEntity entity);

    /**
     * 批量转换为FileConfigVO列表
     */
    List<FileConfigVO> toVOList(List<SysFileConfigEntity> entities);

}
