package ink.charter.website.domain.admin.api.repository;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysResourceEntity;
import ink.charter.website.domain.admin.api.dto.resource.PageResourceDTO;

import java.util.List;

/**
 * 系统资源领域仓库
 *
 * @author charter
 * @create 2025/11/05
 */
public interface SysResourceRepository {

    /**
     * 分页查询资源
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    PageResult<SysResourceEntity> pageResources(PageResourceDTO pageRequest);

    /**
     * 根据ID查询资源
     *
     * @param id 资源ID
     * @return 资源信息
     */
    SysResourceEntity getById(Long id);

    /**
     * 根据资源编码查询资源
     *
     * @param resourceCode 资源编码
     * @return 资源信息
     */
    SysResourceEntity getByResourceCode(String resourceCode);

    /**
     * 根据所属模块查询资源列表
     *
     * @param module 所属模块
     * @return 资源列表
     */
    List<SysResourceEntity> listByModule(String module);
    
    /**
     * 查询所有模块列表
     *
     * @return 模块列表
     */
    List<String> listAllModules();

    /**
     * 查询所有启用的资源
     *
     * @return 资源列表
     */
    List<SysResourceEntity> listAllEnabled();

    /**
     * 根据ID列表批量查询资源
     *
     * @param resourceIds 资源ID列表
     * @return 资源列表
     */
    List<SysResourceEntity> listByIds(List<Long> resourceIds);

    /**
     * 创建资源
     *
     * @param resource 资源信息
     * @return 是否创建成功
     */
    boolean create(SysResourceEntity resource);

    /**
     * 更新资源
     *
     * @param resource 资源信息
     * @return 是否更新成功
     */
    boolean update(SysResourceEntity resource);

    /**
     * 删除资源
     *
     * @param id 资源ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除资源
     *
     * @param ids 资源ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 更新资源状态
     *
     * @param id 资源ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 检查资源编码是否存在
     *
     * @param resourceCode 资源编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByResourceCode(String resourceCode, Long excludeId);
}
