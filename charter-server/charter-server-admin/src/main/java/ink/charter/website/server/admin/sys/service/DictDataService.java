package ink.charter.website.server.admin.sys.service;

import ink.charter.website.common.core.entity.sys.SysDictDataEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 字典数据服务接口
 *
 * @author charter
 * @create 2025/09/15
 */
public interface DictDataService {

    /**
     * 分页查询字典数据
     *
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param status 状态
     * @return 分页结果
     */
    Page<SysDictDataEntity> listPage(Integer pageNo, Integer pageSize, String dictType, String dictLabel, Integer status);

    /**
     * 根据ID查询字典数据
     *
     * @param id 字典数据ID
     * @return 字典数据信息
     */
    SysDictDataEntity getById(Long id);

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    List<SysDictDataEntity> listByDictType(String dictType);

    /**
     * 创建字典数据
     *
     * @param dictData 字典数据信息
     * @return 是否创建成功
     */
    boolean create(SysDictDataEntity dictData);

    /**
     * 更新字典数据
     *
     * @param dictData 字典数据信息
     * @return 是否更新成功
     */
    boolean update(SysDictDataEntity dictData);

    /**
     * 删除字典数据
     *
     * @param id 字典数据ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除字典数据
     *
     * @param ids 字典数据ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 更新字典数据状态
     *
     * @param id 字典数据ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 检查字典键值是否存在
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByDictValue(String dictType, String dictValue, Long excludeId);

    /**
     * 根据字典类型删除所有字典数据
     *
     * @param dictType 字典类型
     * @return 是否删除成功
     */
    boolean deleteByDictType(String dictType);
}