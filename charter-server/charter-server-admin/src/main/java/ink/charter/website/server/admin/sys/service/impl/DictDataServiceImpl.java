package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.entity.sys.SysDictDataEntity;
import ink.charter.website.domain.admin.api.repository.SysDictDataRepository;
import ink.charter.website.server.admin.sys.service.DictDataService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典数据服务实现类
 *
 * @author charter
 * @create 2025/09/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictDataServiceImpl implements DictDataService {

    private final SysDictDataRepository sysDictDataRepository;

    @Override
    public List<SysDictDataEntity> list(String dictType, String dictLabel, Integer status) {
        return sysDictDataRepository.list(dictType, dictLabel, status);
    }

    @Override
    public SysDictDataEntity getById(Long id) {
        return sysDictDataRepository.getById(id);
    }

    @Override
    public List<SysDictDataEntity> listByDictType(String dictType) {
        return sysDictDataRepository.listByDictType(dictType);
    }

    @Override
    public boolean create(SysDictDataEntity dictData) {
        // 检查字典键值是否已存在
        if (existsByDictValue(dictData.getDictType(), dictData.getDictValue(), null)) {
            log.warn("字典键值已存在: {} - {}", dictData.getDictType(), dictData.getDictValue());
            return false;
        }
        return sysDictDataRepository.create(dictData);
    }

    @Override
    public boolean update(SysDictDataEntity dictData) {
        // 检查字典键值是否已存在（排除当前记录）
        if (existsByDictValue(dictData.getDictType(), dictData.getDictValue(), dictData.getId())) {
            log.warn("字典键值已存在: {} - {}", dictData.getDictType(), dictData.getDictValue());
            return false;
        }
        return sysDictDataRepository.update(dictData);
    }

    @Override
    public boolean deleteById(Long id) {
        return sysDictDataRepository.deleteById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return sysDictDataRepository.batchDelete(ids);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return sysDictDataRepository.updateStatus(id, status);
    }

    @Override
    public boolean existsByDictValue(String dictType, String dictValue, Long excludeId) {
        return sysDictDataRepository.existsByDictValue(dictType, dictValue, excludeId);
    }

    @Override
    public boolean deleteByDictType(String dictType) {
        return sysDictDataRepository.deleteByDictType(dictType);
    }
}