package ink.charter.website.server.admin.sys.service;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysOptLogEntity;
import ink.charter.website.domain.admin.api.dto.optlog.PageOptLogDTO;

import java.util.List;

/**
 * 操作日志服务接口
 *
 * @author charter
 * @create 2025/11/12
 */
public interface OptLogService {

    /**
     * 分页查询操作日志
     *
     * @param pageRequest 分页查询参数
     * @return 分页结果
     */
    PageResult<SysOptLogEntity> pageOptLogs(PageOptLogDTO pageRequest);

    /**
     * 根据ID查询操作日志
     *
     * @param id 日志ID
     * @return 操作日志信息
     */
    SysOptLogEntity getById(Long id);

    /**
     * 创建操作日志
     *
     * @param optLog 操作日志信息
     * @return 是否创建成功
     */
    boolean create(SysOptLogEntity optLog);

    /**
     * 批量删除操作日志
     *
     * @param ids 日志ID列表
     * @return 是否删除成功
     */
    boolean batchDelete(List<Long> ids);

    /**
     * 根据ID删除操作日志
     *
     * @param id 日志ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 清空操作日志
     *
     * @return 是否清空成功
     */
    boolean truncate();
}
