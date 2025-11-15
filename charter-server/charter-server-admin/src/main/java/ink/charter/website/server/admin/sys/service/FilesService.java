package ink.charter.website.server.admin.sys.service;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysFilesEntity;
import ink.charter.website.domain.admin.api.dto.files.PageFilesDTO;
import ink.charter.website.domain.admin.api.vo.files.FilesVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件信息Service接口
 *
 * @author charter
 * @create 2025/11/15
 */
public interface FilesService {

    /**
     * 分页查询文件
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    PageResult<FilesVO> pageFiles(PageFilesDTO pageRequest);

    /**
     * 根据ID查询文件
     *
     * @param id 文件ID
     * @return 文件信息
     */
    FilesVO getById(Long id);

    /**
     * 根据MD5查询文件
     *
     * @param fileMd5 文件MD5值
     * @return 文件信息
     */
    FilesVO getByMd5(String fileMd5);

    /**
     * 根据用户ID查询文件列表
     *
     * @param uploadUserId 上传用户ID
     * @return 文件列表
     */
    List<FilesVO> listByUserId(Long uploadUserId);

    /**
     * 根据文件类型查询文件列表
     *
     * @param fileType 文件类型
     * @return 文件列表
     */
    List<FilesVO> listByFileType(String fileType);

    /**
     * 创建文件
     *
     * @param file 文件信息
     * @return 是否创建成功
     */
    boolean create(SysFilesEntity file);

    /**
     * 更新文件
     *
     * @param file 文件信息
     * @return 是否更新成功
     */
    boolean update(SysFilesEntity file);

    /**
     * 删除文件
     *
     * @param id 文件ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 批量删除文件
     *
     * @param ids 文件ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 更新文件状态
     *
     * @param id 文件ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 更新文件最后访问时间
     *
     * @param id 文件ID
     * @param lastAccessTime 最后访问时间
     * @return 是否更新成功
     */
    boolean updateLastAccessTime(Long id, LocalDateTime lastAccessTime);

}
