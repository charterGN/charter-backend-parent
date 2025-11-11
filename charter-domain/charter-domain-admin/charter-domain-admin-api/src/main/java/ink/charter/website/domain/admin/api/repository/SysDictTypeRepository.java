package ink.charter.website.domain.admin.api.repository;

import ink.charter.website.common.core.entity.sys.SysDictTypeEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.domain.admin.api.dto.dict.PageDictTypeDTO;

import java.util.List;

/**
 * 字典类型领域仓库
 *
 * @author charter
 * @create 2025/09/15
 */
public interface SysDictTypeRepository {

    /**
     * 分页查询字典类型
     *
     * @param pageDictTypeDTO 分页参数
     * @return 分页结果
     */
    Page<SysDictTypeEntity> listPage(PageDictTypeDTO pageDictTypeDTO);

    /**
     * 根据ID查询字典类型
     *
     * @param id 字典类型ID
     * @return 字典类型信息
     */
    SysDictTypeEntity getById(Long id);

    /**
     * 根据字典类型查询
     *
     * @param dictType 字典类型
     * @return 字典类型信息
     */
    SysDictTypeEntity getByDictType(String dictType);

    /**
     * 创建字典类型
     *
     * @param dictType 字典类型信息
     * @return 是否创建成功
     */
    boolean create(SysDictTypeEntity dictType);

    /**
     * 更新字典类型
     *
     * @param dictType 字典类型信息
     * @return 是否更新成功
     */
    boolean update(SysDictTypeEntity dictType);

    /**
     * 删除字典类型
     *
     * @param id 字典类型ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除字典类型
     *
     * @param ids 字典类型ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 更新字典类型状态
     *
     * @param id 字典类型ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 查询所有字典类型列表
     *
     * @return 字典类型列表
     */
    List<SysDictTypeEntity> listAll();

    /**
     * 检查字典类型是否存在
     *
     * @param dictType 字典类型
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByDictType(String dictType, Long excludeId);
}