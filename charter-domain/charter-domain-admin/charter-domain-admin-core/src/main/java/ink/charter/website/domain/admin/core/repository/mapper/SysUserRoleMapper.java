package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.core.entity.sys.SysUserRoleEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户角色关联Mapper
 *
 * @author charter
 * @create 2025/11/05
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleEntity> {

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    default List<SysUserRoleEntity> selectByUserId(Long userId) {
        return selectList(QueryWrappers.<SysUserRoleEntity>lambdaQuery()
                .eq(SysUserRoleEntity::getUserId, userId));
    }

    /**
     * 根据用户ID删除关联
     *
     * @param userId 用户ID
     * @return 删除数量
     */
    default int deleteByUserId(Long userId) {
        return delete(QueryWrappers.<SysUserRoleEntity>lambdaQuery()
                .eq(SysUserRoleEntity::getUserId, userId));
    }
}
