package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageRequest;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.common.file.mapper.SysFilesMapper;
import ink.charter.website.domain.admin.api.dto.files.PageFilesDTO;
import ink.charter.website.domain.admin.api.repository.SysFilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件信息Repository实现类
 *
 * @author charter
 * @create 2025/11/15
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysFilesRepositoryImpl implements SysFilesRepository {

    private final SysFilesMapper sysFilesMapper;

    @Override
    public PageResult<SysFilesEntity> pageFiles(PageFilesDTO pageRequest) {
        PageRequest pageParam = pageRequest.getPageRequest();
        Page<SysFilesEntity> page = sysFilesMapper.pageFiles(
            new Page<>(pageParam.getPageNo(), pageParam.getPageSize()),
            pageRequest.getFileName(),
            pageRequest.getFileType(),
            pageRequest.getUploadUserId(),
            pageRequest.getStatus(),
            pageRequest.getUploadTimeStart(),
            pageRequest.getUploadTimeEnd()
        );
        return PageResult.of(page.getRecords(), page.getTotal());
    }

    @Override
    public SysFilesEntity getById(Long id) {
        return sysFilesMapper.selectById(id);
    }

    @Override
    public SysFilesEntity getByMd5(String fileMd5) {
        return sysFilesMapper.selectByMd5(fileMd5);
    }

    @Override
    public SysFilesEntity getByFilePath(String filePath) {
        return sysFilesMapper.selectByFilePath(filePath);
    }

    @Override
    public List<SysFilesEntity> listByUserId(Long uploadUserId) {
        return sysFilesMapper.selectByUserId(uploadUserId);
    }

    @Override
    public List<SysFilesEntity> listByFileType(String fileType) {
        return sysFilesMapper.selectByFileType(fileType);
    }

    @Override
    public boolean create(SysFilesEntity file) {
        return sysFilesMapper.insert(file) > 0;
    }

    @Override
    public boolean update(SysFilesEntity file) {
        return sysFilesMapper.updateById(file) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return sysFilesMapper.deleteById(id) > 0;
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return sysFilesMapper.deleteByIds(ids) > 0;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return sysFilesMapper.updateStatus(id, status) > 0;
    }

    @Override
    public boolean updateLastAccessTime(Long id, LocalDateTime lastAccessTime) {
        return sysFilesMapper.updateLastAccessTime(id, lastAccessTime) > 0;
    }

}
