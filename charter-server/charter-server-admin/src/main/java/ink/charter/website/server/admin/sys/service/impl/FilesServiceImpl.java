package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.domain.admin.api.dto.files.PageFilesDTO;
import ink.charter.website.domain.admin.api.repository.SysFilesRepository;
import ink.charter.website.domain.admin.api.vo.files.FilesVO;
import ink.charter.website.server.admin.sys.converter.FilesConverter;
import ink.charter.website.server.admin.sys.service.FilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件信息Service实现类
 *
 * @author charter
 * @create 2025/11/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {

    private final SysFilesRepository sysFilesRepository;
    private final FilesConverter filesConverter;

    @Override
    public PageResult<FilesVO> pageFiles(PageFilesDTO pageRequest) {
        PageResult<SysFilesEntity> pageResult = sysFilesRepository.pageFiles(pageRequest);
        List<FilesVO> voList = filesConverter.toVOList(pageResult.getRecords());
        return PageResult.of(voList, pageResult.getTotal());
    }

    @Override
    public FilesVO getById(Long id) {
        SysFilesEntity entity = sysFilesRepository.getById(id);
        return entity != null ? filesConverter.toVO(entity) : null;
    }

    @Override
    public FilesVO getByMd5(String fileMd5) {
        SysFilesEntity entity = sysFilesRepository.getByMd5(fileMd5);
        return entity != null ? filesConverter.toVO(entity) : null;
    }

    @Override
    public List<FilesVO> listByUserId(Long uploadUserId) {
        List<SysFilesEntity> entities = sysFilesRepository.listByUserId(uploadUserId);
        return filesConverter.toVOList(entities);
    }

    @Override
    public List<FilesVO> listByFileType(String fileType) {
        List<SysFilesEntity> entities = sysFilesRepository.listByFileType(fileType);
        return filesConverter.toVOList(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(SysFilesEntity file) {
        if (file.getUploadTime() == null) {
            file.setUploadTime(LocalDateTime.now());
        }
        if (file.getStatus() == null) {
            file.setStatus(1);
        }
        return sysFilesRepository.create(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysFilesEntity file) {
        return sysFilesRepository.update(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        return sysFilesRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> ids) {
        return sysFilesRepository.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Integer status) {
        return sysFilesRepository.updateStatus(id, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLastAccessTime(Long id, LocalDateTime lastAccessTime) {
        return sysFilesRepository.updateLastAccessTime(id, lastAccessTime);
    }

}
