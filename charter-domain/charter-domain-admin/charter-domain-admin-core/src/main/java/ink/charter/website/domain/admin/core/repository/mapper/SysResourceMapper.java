package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageRequest;
import ink.charter.website.common.core.entity.sys.SysResourceEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.admin.api.dto.resource.PageResourceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统资源Mapper
 *
 * @author charter
 * @create 2025/11/05
 */
@Mapper
public interface SysResourceMapper extends BaseMapper<SysResourceEntity> {

    /**
     * 分页查询资源
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    default IPage<SysResourceEntity> pageResources(PageResourceDTO pageRequest) {
        PageRequest page = pageRequest.getPageRequest();
        return selectPage(new Page<>(page.getPageNo(), page.getPageSize()), QueryWrappers.<SysResourceEntity>lambdaQuery()
            .likeIfPresent(SysResourceEntity::getResourceName, pageRequest.getResourceName())
            .eqIfPresent(SysResourceEntity::getResourceType, pageRequest.getResourceType())
            .eqIfPresent(SysResourceEntity::getStatus, pageRequest.getStatus())
            .orderByDesc(SysResourceEntity::getCreateTime));
    }

    /**
     * 根据资源编码查询资源
     *
     * @param resourceCode 资源编码
     * @return 资源信息
     */
    default SysResourceEntity selectByResourceCode(String resourceCode) {
        if (!StringUtils.hasText(resourceCode)) {
            return null;
        }
        return selectOne(QueryWrappers.<SysResourceEntity>lambdaQuery()
            .eq(SysResourceEntity::getResourceCode, resourceCode));
    }

    /**
     * 根据资源类型查询资源列表
     *
     * @param resourceType 资源类型
     * @return 资源列表
     */
    default List<SysResourceEntity> selectByResourceType(Integer resourceType) {
        if (resourceType == null) {
            return List.of();
        }
        return selectList(QueryWrappers.<SysResourceEntity>lambdaQuery()
            .eq(SysResourceEntity::getResourceType, resourceType)
            .eq(SysResourceEntity::getStatus, 1)
            .orderByDesc(SysResourceEntity::getCreateTime));
    }

    /**
     * 查询所有启用的资源
     *
     * @return 资源列表
     */
    default List<SysResourceEntity> selectAllEnabled() {
        return selectList(QueryWrappers.<SysResourceEntity>lambdaQuery()
            .eq(SysResourceEntity::getStatus, 1)
            .orderByAsc(SysResourceEntity::getResourceType)
            .orderByDesc(SysResourceEntity::getCreateTime));
    }

    /**
     * 检查资源编码是否存在
     *
     * @param resourceCode 资源编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    default boolean existsByResourceCode(String resourceCode, Long excludeId) {
        if (!StringUtils.hasText(resourceCode)) {
            return false;
        }
        return selectCount(QueryWrappers.<SysResourceEntity>lambdaQuery()
            .eq(SysResourceEntity::getResourceCode, resourceCode)
            .neIfPresent(SysResourceEntity::getId, excludeId)) > 0;
    }

    /**
     * 更新资源状态
     *
     * @param id 资源ID
     * @param status 状态
     * @return 更新行数
     */
    default int updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return 0;
        }
        return update(null, QueryWrappers.<SysResourceEntity>lambdaUpdate()
            .set(SysResourceEntity::getStatus, status)
            .eq(SysResourceEntity::getId, id));
    }
}
