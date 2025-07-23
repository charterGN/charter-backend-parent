package ink.charter.website.common.log.service;

import ink.charter.website.common.core.entity.sys.SysOptLogEntity;

/**
 * 操作日志服务接口
 *
 * @author charter
 * @create 2025/07/17
 */
public interface OperationLogService {

    /**
     * 保存操作日志
     *
     * @param logEntity 日志实体
     */
    void saveLog(SysOptLogEntity logEntity);

    /**
     * 异步保存操作日志
     *
     * @param logEntity 日志实体
     */
    void saveLogAsync(SysOptLogEntity logEntity);
}