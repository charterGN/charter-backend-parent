package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.domain.admin.api.dto.files.CreateFilesDTO;
import ink.charter.website.domain.admin.api.dto.files.UpdateFilesDTO;
import ink.charter.website.domain.admin.api.vo.files.FilesVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 文件信息转换器
 *
 * @author charter
 * @create 2025/11/15
 */
@Mapper(componentModel = "spring")
public interface FilesConverter {

    FilesConverter INSTANCE = Mappers.getMapper(FilesConverter.class);

    /**
     * CreateFilesDTO转换为SysFilesEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    @Mapping(target = "lastAccessTime", ignore = true)
    SysFilesEntity toEntity(CreateFilesDTO dto);

    /**
     * UpdateFilesDTO转换为SysFilesEntity
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "param", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "fileMd5", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "uploadUserId", ignore = true)
    @Mapping(target = "uploadTime", ignore = true)
    @Mapping(target = "lastAccessTime", ignore = true)
    SysFilesEntity toEntity(UpdateFilesDTO dto);

    /**
     * SysFilesEntity转换为FilesVO
     */
    FilesVO toVO(SysFilesEntity entity);

    /**
     * 批量转换为FilesVO列表
     */
    List<FilesVO> toVOList(List<SysFilesEntity> entities);

}
