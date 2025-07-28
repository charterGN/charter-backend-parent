package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.core.entity.sys.SysRoleMenuEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统角色菜单关联Mapper接口
 *
 * @author charter
 * @create 2025/07/28
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuEntity> {

    /**
     * 根据角色ID查询菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    default List<SysRoleMenuEntity> selectByRoleId(Long roleId) {
        return selectList(QueryWrappers.<SysRoleMenuEntity>lambdaQuery()
                .eq(SysRoleMenuEntity::getRoleId, roleId));
    }

    /**
     * 根据角色ID删除关联
     *
     * @param roleId 角色ID
     * @return 删除数量
     */
    default int deleteByRoleId(Long roleId) {
        return delete(QueryWrappers.<SysRoleMenuEntity>lambdaQuery()
                .eq(SysRoleMenuEntity::getRoleId, roleId));
    }
}