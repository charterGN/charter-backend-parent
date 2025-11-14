package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.entity.sys.SysFileConfigEntity;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.domain.admin.api.dto.fileconfig.PageFileConfigDTO;
import ink.charter.website.domain.admin.api.repository.SysFileConfigRepository;
import ink.charter.website.domain.admin.api.vo.fileconfig.FileConfigVO;
import ink.charter.website.server.admin.sys.converter.FileConfigConverter;
import ink.charter.website.server.admin.sys.service.FileConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件配置Service实现
 *
 * @author charter
 * @create 2025/11/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileConfigServiceImpl implements FileConfigService {

    private final SysFileConfigRepository sysFileConfigRepository;
    private final FileConfigConverter fileConfigConverter;

    @Override
    public PageResult<FileConfigVO> pageFileConfigs(PageFileConfigDTO pageRequest) {
        PageResult<SysFileConfigEntity> pageResult = sysFileConfigRepository.pageFileConfigs(pageRequest);
        
        List<FileConfigVO> voList = pageResult.getRecords().stream()
            .map(fileConfigConverter::toVO)
            .collect(Collectors.toList());
        
        return PageResult.of(voList, pageResult.getTotal());
    }

    @Override
    public FileConfigVO getById(Long id) {
        SysFileConfigEntity entity = sysFileConfigRepository.getById(id);
        return entity != null ? fileConfigConverter.toVO(entity) : null;
    }

    @Override
    public FileConfigVO getEnabledConfig() {
        SysFileConfigEntity entity = sysFileConfigRepository.getEnabledConfig();
        return entity != null ? fileConfigConverter.toVO(entity) : null;
    }

    @Override
    public List<FileConfigVO> listAll() {
        List<SysFileConfigEntity> entities = sysFileConfigRepository.listAll();
        return entities.stream()
            .map(fileConfigConverter::toVO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(SysFileConfigEntity fileConfig) {
        // 如果新配置启用，则先禁用所有其他配置
        if (fileConfig.getEnabled() != null && fileConfig.getEnabled() == 1) {
            sysFileConfigRepository.disableAll();
        }
        return sysFileConfigRepository.create(fileConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(SysFileConfigEntity fileConfig) {
        // 如果更新为启用状态，则先禁用所有其他配置
        if (fileConfig.getEnabled() != null && fileConfig.getEnabled() == 1) {
            sysFileConfigRepository.disableAll();
        }
        return sysFileConfigRepository.update(fileConfig);
    }

    @Override
    public boolean deleteById(Long id) {
        return sysFileConfigRepository.deleteById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return sysFileConfigRepository.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnabled(Long id, Integer enabled) {
        // 如果启用，则先禁用所有其他配置
        if (enabled != null && enabled == 1) {
            sysFileConfigRepository.disableAll();
        }
        return sysFileConfigRepository.updateEnabled(id, enabled);
    }

}
