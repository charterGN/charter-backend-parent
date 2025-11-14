package ink.charter.website.server.admin.sys.service;

import ink.charter.website.common.core.entity.sys.SysFileConfigEntity;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.domain.admin.api.dto.fileconfig.PageFileConfigDTO;
import ink.charter.website.domain.admin.api.vo.fileconfig.FileConfigVO;

import java.util.List;

/**
 * 文件配置Service接口
 *
 * @author charter
 * @create 2025/11/14
 */
public interface FileConfigService {

    /**
     * 分页查询文件配置
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    PageResult<FileConfigVO> pageFileConfigs(PageFileConfigDTO pageRequest);

    /**
     * 根据ID查询文件配置
     *
     * @param id 配置ID
     * @return 文件配置信息
     */
    FileConfigVO getById(Long id);

    /**
     * 查询启用的文件配置
     *
     * @return 启用的文件配置
     */
    FileConfigVO getEnabledConfig();

    /**
     * 查询所有文件配置列表
     *
     * @return 文件配置列表
     */
    List<FileConfigVO> listAll();

    /**
     * 创建文件配置
     *
     * @param fileConfig 文件配置信息
     * @return 是否创建成功
     */
    boolean create(SysFileConfigEntity fileConfig);

    /**
     * 更新文件配置
     *
     * @param fileConfig 文件配置信息
     * @return 是否更新成功
     */
    boolean update(SysFileConfigEntity fileConfig);

    /**
     * 删除文件配置
     *
     * @param id 配置ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除文件配置
     *
     * @param ids 配置ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 更新文件配置启用状态
     *
     * @param id 配置ID
     * @param enabled 启用状态
     * @return 是否更新成功
     */
    boolean updateEnabled(Long id, Integer enabled);

}
