package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageRequest;
import ink.charter.website.common.core.entity.sys.SysDictTypeEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.admin.api.dto.dict.PageDictTypeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字典类型Mapper
 *
 * @author charter
 * @create 2025/09/15
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictTypeEntity> {

    /**
     * 分页查询字典类型
     *
     * @param pageRequest 分页参数
     * @return 分页结果
     */
    default Page<SysDictTypeEntity> pageDictType(PageDictTypeDTO pageRequest) {
        PageRequest page = pageRequest.getPageRequest();
        return selectPage(new Page<>(page.getPageNo(), page.getPageSize()), QueryWrappers.<SysDictTypeEntity>lambdaQuery()
            .likeIfPresent(SysDictTypeEntity::getDictName, pageRequest.getDictName())
            .likeIfPresent(SysDictTypeEntity::getDictType, pageRequest.getDictType())
            .eqIfPresent(SysDictTypeEntity::getStatus, pageRequest.getStatus())
            .orderByDesc(SysDictTypeEntity::getCreateTime));
    }

    /**
     * 根据字典类型查询
     *
     * @param dictType 字典类型
     * @return 字典类型信息
     */
    default SysDictTypeEntity selectByDictType(String dictType) {
        if (!StringUtils.hasText(dictType)) {
            return null;
        }
        return selectOne(QueryWrappers.<SysDictTypeEntity>lambdaQuery()
            .eq(SysDictTypeEntity::getDictType, dictType));
    }

    /**
     * 查询所有字典类型列表
     *
     * @return 字典类型列表
     */
    default List<SysDictTypeEntity> selectAll() {
        return selectList(QueryWrappers.<SysDictTypeEntity>lambdaQuery()
            .eq(SysDictTypeEntity::getStatus, 1)
            .orderByAsc(SysDictTypeEntity::getDictName));
    }

    /**
     * 检查字典类型是否存在
     *
     * @param dictType 字典类型
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    default boolean existsByDictType(String dictType, Long excludeId) {
        if (!StringUtils.hasText(dictType)) {
            return false;
        }
        return selectCount(QueryWrappers.<SysDictTypeEntity>lambdaQuery()
            .eq(SysDictTypeEntity::getDictType, dictType)
            .neIfPresent(SysDictTypeEntity::getId, excludeId)) > 0;
    }

    /**
     * 更新字典类型状态
     *
     * @param id 字典类型ID
     * @param status 状态
     * @return 更新行数
     */
    default int updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return 0;
        }
        return update(null, QueryWrappers.<SysDictTypeEntity>lambdaUpdate()
            .set(SysDictTypeEntity::getStatus, status)
            .eq(SysDictTypeEntity::getId, id));
    }
}