package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageRequest;
import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.admin.api.dto.menu.PageMenuDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统菜单Mapper接口
 *
 * @author charter
 * @create 2025/07/28
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {

    /**
     * 分页查询菜单
     *
     * @param pageRequest 分页参数
     * @return 分页结果
     */
    default IPage<SysMenuEntity> pageResources(PageMenuDTO pageRequest) {
        PageRequest page = pageRequest.getPageRequest();
        return selectPage(new Page<>(page.getPageNo(), page.getPageSize()), QueryWrappers.<SysMenuEntity>lambdaQuery()
                .likeIfPresent(SysMenuEntity::getMenuName, pageRequest.getMenuName())
                .eqIfPresent(SysMenuEntity::getStatus, pageRequest.getStatus())
                .orderByDesc(SysMenuEntity::getCreateTime));
    }

    /**
     * 查询所有正常状态的菜单
     *
     * @return 菜单列表
     */
    default List<SysMenuEntity> selectNormalMenus() {
        return selectList(QueryWrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getStatus, 1)
                .orderByAsc(SysMenuEntity::getSortOrder));
    }

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    default List<SysMenuEntity> selectAll() {
        return selectList(QueryWrappers.<SysMenuEntity>lambdaQuery()
                .orderByAsc(SysMenuEntity::getSortOrder)
                .orderByDesc(SysMenuEntity::getCreateTime));
    }

    /**
     * 根据父菜单ID查询子菜单列表
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    default List<SysMenuEntity> selectByParentId(Long parentId) {
        if (parentId == null) {
            return List.of();
        }
        return selectList(QueryWrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getParentId, parentId)
                .orderByAsc(SysMenuEntity::getSortOrder));
    }

    /**
     * 更新菜单状态
     *
     * @param menuId 菜单ID
     * @param status 状态
     * @return 更新行数
     */
    default int updateStatus(Long menuId, Integer status) {
        if (menuId == null || status == null) {
            return 0;
        }
        return update(null, QueryWrappers.<SysMenuEntity>lambdaUpdate()
                .set(SysMenuEntity::getStatus, status)
                .eq(SysMenuEntity::getId, menuId));
    }
}