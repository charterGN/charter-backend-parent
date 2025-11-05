package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageRequest;
import ink.charter.website.common.core.entity.sys.SysRoleEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.admin.api.dto.role.PageRoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统角色Mapper
 *
 * @author charter
 * @create 2025/11/05
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

    /**
     * 分页查询角色
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    default IPage<SysRoleEntity> pageRoles(PageRoleDTO pageRequest) {
        PageRequest page = pageRequest.getPageRequest();
        return selectPage(new Page<>(page.getPageNo(), page.getPageSize()), QueryWrappers.<SysRoleEntity>lambdaQuery()
            .likeIfPresent(SysRoleEntity::getRoleName, pageRequest.getRoleName())
            .likeIfPresent(SysRoleEntity::getRoleCode, pageRequest.getRoleCode())
            .eqIfPresent(SysRoleEntity::getStatus, pageRequest.getStatus())
            .orderByAsc(SysRoleEntity::getSortOrder)
            .orderByDesc(SysRoleEntity::getCreateTime));
    }

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    default SysRoleEntity selectByRoleCode(String roleCode) {
        if (!StringUtils.hasText(roleCode)) {
            return null;
        }
        return selectOne(QueryWrappers.<SysRoleEntity>lambdaQuery()
            .eq(SysRoleEntity::getRoleCode, roleCode));
    }

    /**
     * 查询所有启用的角色
     *
     * @return 角色列表
     */
    default List<SysRoleEntity> selectAllEnabled() {
        return selectList(QueryWrappers.<SysRoleEntity>lambdaQuery()
            .eq(SysRoleEntity::getStatus, 1)
            .orderByAsc(SysRoleEntity::getSortOrder)
            .orderByDesc(SysRoleEntity::getCreateTime));
    }

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Select("""
            SELECT DISTINCT sr.*
            FROM sys_role sr
            INNER JOIN sys_user_role sur ON sr.id = sur.role_id
            WHERE sur.user_id = #{userId}
            AND sr.is_deleted = 0
            AND sur.is_deleted = 0
            AND sr.status = 1
            ORDER BY sr.sort_order, sr.create_time DESC
            """)
    List<SysRoleEntity> selectByUserId(@Param("userId") Long userId);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    default boolean existsByRoleCode(String roleCode, Long excludeId) {
        if (!StringUtils.hasText(roleCode)) {
            return false;
        }
        return selectCount(QueryWrappers.<SysRoleEntity>lambdaQuery()
            .eq(SysRoleEntity::getRoleCode, roleCode)
            .neIfPresent(SysRoleEntity::getId, excludeId)) > 0;
    }

    /**
     * 更新角色状态
     *
     * @param id 角色ID
     * @param status 状态
     * @return 更新行数
     */
    default int updateStatus(Long id, Integer status) {
        if (id == null || status == null) {
            return 0;
        }
        return update(null, QueryWrappers.<SysRoleEntity>lambdaUpdate()
            .set(SysRoleEntity::getStatus, status)
            .eq(SysRoleEntity::getId, id));
    }
}
