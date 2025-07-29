package ink.charter.website.common.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统文件Mapper
 *
 * @author charter
 * @create 2025/07/17
 */
@Mapper
public interface SysFilesMapper extends BaseMapper<SysFilesEntity> {

    /**
     * 根据MD5查询文件
     *
     * @param fileMd5 文件MD5值
     * @return 文件信息
     */
    default SysFilesEntity selectByMd5(String fileMd5) {
        return selectOne(Wrappers.<SysFilesEntity>lambdaQuery()
                .eq(SysFilesEntity::getFileMd5, fileMd5)
                .eq(SysFilesEntity::getStatus, 1));
    }

    /**
     * 根据用户ID查询文件列表
     *
     * @param uploadUserId 上传用户ID
     * @return 文件列表
     */
    default List<SysFilesEntity> selectByUserId(Long uploadUserId) {
        return selectList(Wrappers.<SysFilesEntity>lambdaQuery()
                .eq(SysFilesEntity::getUploadUserId, uploadUserId)
                .eq(SysFilesEntity::getStatus, 1)
                .orderByDesc(SysFilesEntity::getUploadTime));
    }

    /**
     * 根据文件类型查询文件列表
     *
     * @param fileType 文件类型
     * @return 文件列表
     */
    default List<SysFilesEntity> selectByFileType(String fileType) {
        return selectList(Wrappers.<SysFilesEntity>lambdaQuery()
                .eq(SysFilesEntity::getFileType, fileType)
                .eq(SysFilesEntity::getStatus, 1)
                .orderByDesc(SysFilesEntity::getUploadTime));
    }

    /**
     * 更新文件最后访问时间
     *
     * @param id 文件ID
     * @param lastAccessTime 最后访问时间
     * @return 更新结果
     */
    default int updateLastAccessTime(Long id, LocalDateTime lastAccessTime) {
        SysFilesEntity entity = new SysFilesEntity();
        entity.setId(id);
        entity.setLastAccessTime(lastAccessTime);
        return updateById(entity);
    }

    /**
     * 根据文件路径查询文件
     *
     * @param filePath 文件路径
     * @return 文件信息
     */
    default SysFilesEntity selectByFilePath(String filePath) {
        return selectOne(Wrappers.<SysFilesEntity>lambdaQuery()
                .eq(SysFilesEntity::getFilePath, filePath)
                .eq(SysFilesEntity::getStatus, 1));
    }

}