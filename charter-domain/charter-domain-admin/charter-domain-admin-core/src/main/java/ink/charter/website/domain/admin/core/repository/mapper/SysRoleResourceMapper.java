package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.core.entity.sys.SysRoleResourceEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色资源关联Mapper
 *
 * @author charter
 * @create 2025/11/05
 */
@Mapper
public interface SysRoleResourceMapper extends BaseMapper<SysRoleResourceEntity> {

    /**
     * 根据角色ID查询资源ID列表
     *
     * @param roleId 角色ID
     * @return 角色资源关联列表
     */
    default List<SysRoleResourceEntity> selectByRoleId(Long roleId) {
        return selectList(QueryWrappers.<SysRoleResourceEntity>lambdaQuery()
                .eq(SysRoleResourceEntity::getRoleId, roleId));
    }

    /**
     * 根据角色ID删除关联
     *
     * @param roleId 角色ID
     * @return 删除数量
     */
    default int deleteByRoleId(Long roleId) {
        return delete(QueryWrappers.<SysRoleResourceEntity>lambdaQuery()
                .eq(SysRoleResourceEntity::getRoleId, roleId));
    }
}
