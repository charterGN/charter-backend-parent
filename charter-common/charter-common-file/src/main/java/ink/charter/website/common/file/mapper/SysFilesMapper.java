package ink.charter.website.common.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

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
     * 分页查询文件
     * @param page 分页参数
     * @param filename 文件名
     * @param fileType 文件类型
     * @param uploadUserId 上传用户ID
     * @param status 状态
     * @param uploadTimeStart 上传时间开始
     * @param uploadTimeEnd 上传时间结束
     * @return 分页结果
     */
    default Page<SysFilesEntity> pageFiles(Page<SysFilesEntity> page, String filename,
                                           String fileType, Long uploadUserId, Integer status,
                                           LocalDateTime uploadTimeStart, LocalDateTime uploadTimeEnd) {
        return selectPage(page, QueryWrappers.<SysFilesEntity>lambdaQuery()
            .likeIfPresent(SysFilesEntity::getFileName, filename)
            .eqIfPresent(SysFilesEntity::getFileType, fileType)
            .eqIfPresent(SysFilesEntity::getUploadUserId, uploadUserId)
            .eqIfPresent(SysFilesEntity::getStatus, status)
            .geIfPresent(SysFilesEntity::getUploadTime, uploadTimeStart)
            .leIfPresent(SysFilesEntity::getUploadTime, uploadTimeEnd)
            .orderByDesc(SysFilesEntity::getUploadTime));
    }

    /**
     * 根据MD5查询文件
     *
     * @param fileMd5 文件MD5值
     * @return 文件信息
     */
    default SysFilesEntity selectByMd5(String fileMd5) {
        if (!StringUtils.hasText(fileMd5)) {
            return null;
        }
        return selectOne(QueryWrappers.<SysFilesEntity>lambdaQuery()
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
        if (uploadUserId == null) {
            return List.of();
        }
        return selectList(QueryWrappers.<SysFilesEntity>lambdaQuery()
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
        if (!StringUtils.hasText(fileType)) {
            return List.of();
        }
        return selectList(QueryWrappers.<SysFilesEntity>lambdaQuery()
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
        if (!StringUtils.hasText(filePath)) {
            return null;
        }
        return selectOne(QueryWrappers.<SysFilesEntity>lambdaQuery()
            .eq(SysFilesEntity::getFilePath, filePath)
            .eq(SysFilesEntity::getStatus, 1));
    }

    /**
     * 更新文件状态
     *
     * @param id 文件ID
     * @param status 状态
     * @return 更新行数
     */
    default int updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return 0;
        }
        return update(null, QueryWrappers.<SysFilesEntity>lambdaUpdate()
            .set(SysFilesEntity::getStatus, status)
            .eq(SysFilesEntity::getId, id));
    }

}