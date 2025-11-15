package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.common.core.entity.sys.SysUserEntity;
import ink.charter.website.domain.admin.api.dto.files.PageFilesDTO;
import ink.charter.website.domain.admin.api.repository.SysFilesRepository;
import ink.charter.website.domain.admin.api.vo.files.FilesVO;
import ink.charter.website.server.admin.sys.converter.FilesConverter;
import ink.charter.website.server.admin.sys.service.FilesService;
import ink.charter.website.server.admin.sys.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final UserService userService;

    @Override
    public PageResult<FilesVO> pageFiles(PageFilesDTO pageRequest) {
        PageResult<SysFilesEntity> pageResult = sysFilesRepository.pageFiles(pageRequest);
        List<FilesVO> voList = filesConverter.toVOList(pageResult.getRecords());
        // 填充上传用户名
        fillUploadUsername(voList);
        return PageResult.of(voList, pageResult.getTotal());
    }

    @Override
    public FilesVO getById(Long id) {
        SysFilesEntity entity = sysFilesRepository.getById(id);
        if (entity == null) {
            return null;
        }
        FilesVO vo = filesConverter.toVO(entity);
        // 填充上传用户名
        fillUploadUsername(vo);
        return vo;
    }

    @Override
    public FilesVO getByMd5(String fileMd5) {
        SysFilesEntity entity = sysFilesRepository.getByMd5(fileMd5);
        if (entity == null) {
            return null;
        }
        FilesVO vo = filesConverter.toVO(entity);
        // 填充上传用户名
        fillUploadUsername(vo);
        return vo;
    }

    @Override
    public List<FilesVO> listByUserId(Long uploadUserId) {
        List<SysFilesEntity> entities = sysFilesRepository.listByUserId(uploadUserId);
        List<FilesVO> voList = filesConverter.toVOList(entities);
        // 填充上传用户名
        fillUploadUsername(voList);
        return voList;
    }

    @Override
    public List<FilesVO> listByFileType(String fileType) {
        List<SysFilesEntity> entities = sysFilesRepository.listByFileType(fileType);
        List<FilesVO> voList = filesConverter.toVOList(entities);
        // 填充上传用户名
        fillUploadUsername(voList);
        return voList;
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

    /**
     * 填充单个文件的上传用户名
     *
     * @param vo 文件VO
     */
    private void fillUploadUsername(FilesVO vo) {
        if (vo != null && vo.getUploadUserId() != null) {
            SysUserEntity user = userService.getUserById(vo.getUploadUserId());
            if (user != null) {
                vo.setUploadUsername(user.getUsername());
            }
        }
    }

    /**
     * 批量填充文件列表的上传用户名
     *
     * @param voList 文件VO列表
     */
    private void fillUploadUsername(List<FilesVO> voList) {
        if (voList == null || voList.isEmpty()) {
            return;
        }

        // 收集所有需要查询的用户ID（去重）
        List<Long> userIds = voList.stream()
                .map(FilesVO::getUploadUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return;
        }

        // 批量查询用户信息并构建ID到用户名的映射
        Map<Long, String> userIdToNameMap = userService.getUsersByIds(userIds).stream()
                .collect(Collectors.toMap(SysUserEntity::getId, SysUserEntity::getUsername));

        // 填充用户名
        voList.forEach(vo -> {
            if (vo.getUploadUserId() != null) {
                String username = userIdToNameMap.get(vo.getUploadUserId());
                if (username != null) {
                    vo.setUploadUsername(username);
                }
            }
        });
    }

}
