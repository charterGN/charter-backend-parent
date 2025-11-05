package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.entity.sys.SysDictDataEntity;
import ink.charter.website.domain.admin.api.repository.SysDictDataRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysDictDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典数据领域仓库实现
 *
 * @author charter
 * @create 2025/09/15
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysDictDataRepositoryImpl implements SysDictDataRepository {

    private final SysDictDataMapper sysDictDataMapper;

    @Override
    public Page<SysDictDataEntity> listPage(Integer pageNo, Integer pageSize, String dictType, String dictLabel, Integer status) {
        Page<SysDictDataEntity> page = new Page<>(pageNo, pageSize);
        return sysDictDataMapper.selectPage(page, dictType, dictLabel, status);
    }

    @Override
    public SysDictDataEntity getById(Long id) {
        if (id == null) {
            return null;
        }
        return sysDictDataMapper.selectById(id);
    }

    @Override
    public List<SysDictDataEntity> listByDictType(String dictType) {
        return sysDictDataMapper.selectByDictType(dictType);
    }

    @Override
    public boolean create(SysDictDataEntity dictData) {
        if (dictData == null) {
            return false;
        }
        
        try {
            int result = sysDictDataMapper.insert(dictData);
            return result > 0;
        } catch (Exception e) {
            log.error("创建字典数据失败", e);
            return false;
        }
    }

    @Override
    public boolean update(SysDictDataEntity dictData) {
        if (dictData == null || dictData.getId() == null) {
            return false;
        }
        
        try {
            int result = sysDictDataMapper.updateById(dictData);
            return result > 0;
        } catch (Exception e) {
            log.error("更新字典数据失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        
        try {
            int result = sysDictDataMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除字典数据失败", e);
            return false;
        }
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        try {
            int result = sysDictDataMapper.deleteByIds(ids);
            return result > 0;
        } catch (Exception e) {
            log.error("批量删除字典数据失败", e);
            return false;
        }
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return false;
        }
        
        try {
            int result = sysDictDataMapper.updateStatus(id, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新字典数据状态失败", e);
            return false;
        }
    }

    @Override
    public boolean existsByDictValue(String dictType, String dictValue, Long excludeId) {
        return sysDictDataMapper.existsByDictValue(dictType, dictValue, excludeId);
    }

    @Override
    public boolean deleteByDictType(String dictType) {
        try {
            int result = sysDictDataMapper.deleteByDictType(dictType);
            return result >= 0; // 即使没有数据删除也算成功
        } catch (Exception e) {
            log.error("根据字典类型删除字典数据失败", e);
            return false;
        }
    }
}