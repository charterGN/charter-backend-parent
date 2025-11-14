package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.entity.sys.SysFileConfigEntity;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.file.mapper.SysFileConfigMapper;
import ink.charter.website.domain.admin.api.dto.fileconfig.PageFileConfigDTO;
import ink.charter.website.domain.admin.api.repository.SysFileConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文件配置Repository实现
 *
 * @author charter
 * @create 2025/11/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysFileConfigRepositoryImpl implements SysFileConfigRepository {

    private final SysFileConfigMapper sysFileConfigMapper;

    @Override
    public PageResult<SysFileConfigEntity> pageFileConfigs(PageFileConfigDTO pageRequest) {
        Page<SysFileConfigEntity> page = new Page<>(
            pageRequest.getPageRequest().getPageNo(),
            pageRequest.getPageRequest().getPageSize()
        );

        Page<SysFileConfigEntity> resultPage = sysFileConfigMapper.pageFileConfigs(
            page,
            pageRequest.getConfigName(),
            pageRequest.getStorageType(),
            pageRequest.getEnabled()
        );
        
        return PageResult.of(resultPage.getRecords(), resultPage.getTotal());
    }

    @Override
    public SysFileConfigEntity getById(Long id) {
        if (id == null) {
            return null;
        }
        return sysFileConfigMapper.selectById(id);
    }

    @Override
    public SysFileConfigEntity getEnabledConfig() {
        return sysFileConfigMapper.selectEnabledConfig();
    }

    @Override
    public List<SysFileConfigEntity> listAll() {
        return sysFileConfigMapper.listAll();
    }

    @Override
    public boolean create(SysFileConfigEntity fileConfig) {
        if (fileConfig == null) {
            return false;
        }
        
        try {
            int result = sysFileConfigMapper.insert(fileConfig);
            return result > 0;
        } catch (Exception e) {
            log.error("创建文件配置失败", e);
            return false;
        }
    }

    @Override
    public boolean update(SysFileConfigEntity fileConfig) {
        if (fileConfig == null || fileConfig.getId() == null) {
            return false;
        }
        
        try {
            int result = sysFileConfigMapper.updateById(fileConfig);
            return result > 0;
        } catch (Exception e) {
            log.error("更新文件配置失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        
        try {
            int result = sysFileConfigMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除文件配置失败", e);
            return false;
        }
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        try {
            int result = sysFileConfigMapper.deleteByIds(ids);
            return result > 0;
        } catch (Exception e) {
            log.error("批量删除文件配置失败", e);
            return false;
        }
    }

    @Override
    public boolean updateEnabled(Long id, Integer enabled) {
        if (id == null || enabled == null) {
            return false;
        }
        
        try {
            int result = sysFileConfigMapper.updateEnabled(id, enabled);
            return result > 0;
        } catch (Exception e) {
            log.error("更新文件配置启用状态失败", e);
            return false;
        }
    }

    @Override
    public boolean disableAll() {
        try {
            int result = sysFileConfigMapper.disableAll();
            return result >= 0; // 即使没有数据更新也算成功
        } catch (Exception e) {
            log.error("禁用所有文件配置失败", e);
            return false;
        }
    }

}
