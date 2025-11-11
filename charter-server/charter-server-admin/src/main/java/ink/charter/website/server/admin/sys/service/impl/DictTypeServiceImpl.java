package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.entity.sys.SysDictTypeEntity;
import ink.charter.website.domain.admin.api.dto.dict.PageDictTypeDTO;
import ink.charter.website.domain.admin.api.repository.SysDictTypeRepository;
import ink.charter.website.server.admin.sys.service.DictTypeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典类型服务实现类
 *
 * @author charter
 * @create 2025/09/15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictTypeServiceImpl implements DictTypeService {

    private final SysDictTypeRepository sysDictTypeRepository;

    @Override
    public Page<SysDictTypeEntity> listPage(PageDictTypeDTO pageDictTypeDTO) {
        return sysDictTypeRepository.listPage(pageDictTypeDTO);
    }

    @Override
    public SysDictTypeEntity getById(Long id) {
        return sysDictTypeRepository.getById(id);
    }

    @Override
    public SysDictTypeEntity getByDictType(String dictType) {
        return sysDictTypeRepository.getByDictType(dictType);
    }

    @Override
    public boolean create(SysDictTypeEntity dictType) {
        // 检查字典类型是否已存在
        if (existsByDictType(dictType.getDictType(), null)) {
            log.warn("字典类型已存在: {}", dictType.getDictType());
            return false;
        }
        return sysDictTypeRepository.create(dictType);
    }

    @Override
    public boolean update(SysDictTypeEntity dictType) {
        // 检查字典类型是否已存在（排除当前记录）
        if (existsByDictType(dictType.getDictType(), dictType.getId())) {
            log.warn("字典类型已存在: {}", dictType.getDictType());
            return false;
        }
        return sysDictTypeRepository.update(dictType);
    }

    @Override
    public boolean deleteById(Long id) {
        return sysDictTypeRepository.deleteById(id);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return sysDictTypeRepository.batchDelete(ids);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return sysDictTypeRepository.updateStatus(id, status);
    }

    @Override
    public List<SysDictTypeEntity> listAll() {
        return sysDictTypeRepository.listAll();
    }

    @Override
    public boolean existsByDictType(String dictType, Long excludeId) {
        return sysDictTypeRepository.existsByDictType(dictType, excludeId);
    }
}