package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.entity.sys.SysDictTypeEntity;
import ink.charter.website.domain.admin.api.repository.SysDictTypeRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysDictTypeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典类型领域仓库实现
 *
 * @author charter
 * @create 2025/09/15
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysDictTypeRepositoryImpl implements SysDictTypeRepository {

    private final SysDictTypeMapper sysDictTypeMapper;

    @Override
    public Page<SysDictTypeEntity> listPage(Integer pageNo, Integer pageSize, String dictName, String dictType, Integer status) {
        Page<SysDictTypeEntity> page = new Page<>(pageNo, pageSize);
        return sysDictTypeMapper.selectPage(page, dictName, dictType, status);
    }

    @Override
    public SysDictTypeEntity getById(Long id) {
        if (id == null) {
            return null;
        }
        return sysDictTypeMapper.selectById(id);
    }

    @Override
    public SysDictTypeEntity getByDictType(String dictType) {
        return sysDictTypeMapper.selectByDictType(dictType);
    }

    @Override
    public boolean create(SysDictTypeEntity dictType) {
        if (dictType == null) {
            return false;
        }
        
        try {
            int result = sysDictTypeMapper.insert(dictType);
            return result > 0;
        } catch (Exception e) {
            log.error("创建字典类型失败", e);
            return false;
        }
    }

    @Override
    public boolean update(SysDictTypeEntity dictType) {
        if (dictType == null || dictType.getId() == null) {
            return false;
        }
        
        try {
            int result = sysDictTypeMapper.updateById(dictType);
            return result > 0;
        } catch (Exception e) {
            log.error("更新字典类型失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }
        
        try {
            int result = sysDictTypeMapper.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("删除字典类型失败", e);
            return false;
        }
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        try {
            int result = sysDictTypeMapper.deleteBatchIds(ids);
            return result > 0;
        } catch (Exception e) {
            log.error("批量删除字典类型失败", e);
            return false;
        }
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return false;
        }
        
        try {
            int result = sysDictTypeMapper.updateStatus(id, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新字典类型状态失败", e);
            return false;
        }
    }

    @Override
    public List<SysDictTypeEntity> listAll() {
        return sysDictTypeMapper.selectAll();
    }

    @Override
    public boolean existsByDictType(String dictType, Long excludeId) {
        return sysDictTypeMapper.existsByDictType(dictType, excludeId);
    }
}