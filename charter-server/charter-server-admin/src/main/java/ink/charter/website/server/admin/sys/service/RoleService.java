package ink.charter.website.server.admin.sys.service;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysRoleEntity;
import ink.charter.website.domain.admin.api.dto.role.PageRoleDTO;
import ink.charter.website.domain.admin.api.vo.role.RoleVO;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author charter
 * @create 2025/11/05
 */
public interface RoleService {

    /**
     * 分页查询角色
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    PageResult<RoleVO> pageRoles(PageRoleDTO pageRequest);

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色信息
     */
    SysRoleEntity getById(Long id);

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    SysRoleEntity getByRoleCode(String roleCode);

    /**
     * 查询所有启用的角色
     *
     * @return 角色列表
     */
    List<SysRoleEntity> listAllEnabled();

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRoleEntity> listByUserId(Long userId);

    /**
     * 创建角色
     *
     * @param role 角色信息
     * @return 是否创建成功
     */
    boolean create(SysRoleEntity role);

    /**
     * 更新角色
     *
     * @param role 角色信息
     * @return 是否更新成功
     */
    boolean update(SysRoleEntity role);

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除角色
     *
     * @param ids 角色ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 更新角色状态
     *
     * @param id 角色ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 保存用户角色关联
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void saveUserRoles(Long userId, List<Long> roleIds);
}
