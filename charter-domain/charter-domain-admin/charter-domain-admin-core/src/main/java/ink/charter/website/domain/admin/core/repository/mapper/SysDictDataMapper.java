package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.entity.sys.SysDictDataEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字典数据Mapper
 *
 * @author charter
 * @create 2025/09/15
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictDataEntity> {

    /**
     * 分页查询字典数据
     *
     * @param page 分页参数
     * @param dictType 字典类型
     * @param dictLabel 字典标签
     * @param status 状态
     * @return 分页结果
     */
    default Page<SysDictDataEntity> selectPage(Page<SysDictDataEntity> page, String dictType, String dictLabel, Integer status) {
        return selectPage(page, QueryWrappers.<SysDictDataEntity>lambdaQuery()
            .eqIfPresent(SysDictDataEntity::getDictType, dictType)
            .likeIfPresent(SysDictDataEntity::getDictLabel, dictLabel)
            .eqIfPresent(SysDictDataEntity::getStatus, status)
            .orderByAsc(SysDictDataEntity::getSortOrder)
            .orderByDesc(SysDictDataEntity::getCreateTime));
    }

    /**
     * 根据字典类型查询字典数据列表
     *
     * @param dictType 字典类型
     * @return 字典数据列表
     */
    default List<SysDictDataEntity> selectByDictType(String dictType) {
        if (!StringUtils.hasText(dictType)) {
            return List.of();
        }
        return selectList(QueryWrappers.<SysDictDataEntity>lambdaQuery()
            .eq(SysDictDataEntity::getDictType, dictType)
            .eq(SysDictDataEntity::getStatus, 1)
            .orderByAsc(SysDictDataEntity::getSortOrder)
            .orderByDesc(SysDictDataEntity::getCreateTime));
    }

    /**
     * 检查字典键值是否存在
     *
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    default boolean existsByDictValue(String dictType, String dictValue, Long excludeId) {
        if (!StringUtils.hasText(dictType) || !StringUtils.hasText(dictValue)) {
            return false;
        }
        return selectCount(QueryWrappers.<SysDictDataEntity>lambdaQuery()
            .eq(SysDictDataEntity::getDictType, dictType)
            .eq(SysDictDataEntity::getDictValue, dictValue)
            .neIfPresent(SysDictDataEntity::getId, excludeId)) > 0;
    }

    /**
     * 更新字典数据状态
     *
     * @param id 字典数据ID
     * @param status 状态
     * @return 更新行数
     */
    default int updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return 0;
        }
        return update(null, QueryWrappers.<SysDictDataEntity>lambdaUpdate()
            .set(SysDictDataEntity::getStatus, status)
            .eq(SysDictDataEntity::getId, id));
    }

    /**
     * 根据字典类型删除所有字典数据
     *
     * @param dictType 字典类型
     * @return 删除行数
     */
    default int deleteByDictType(String dictType) {
        if (!StringUtils.hasText(dictType)) {
            return 0;
        }
        return delete(QueryWrappers.<SysDictDataEntity>lambdaQuery()
            .eq(SysDictDataEntity::getDictType, dictType));
    }
}