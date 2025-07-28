package ink.charter.website.common.log.service.impl;

import ink.charter.website.common.log.mapper.SysOptLogMapper;
import ink.charter.website.common.log.service.OperationLogService;
import ink.charter.website.common.core.entity.sys.SysOptLogEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务实现类
 * <p>
 *   注意：@RequiredArgsConstructor的作用 ：
 *   <p>- 这是 Lombok 注解，会自动生成包含所有 final 字段的构造函数</p>
 *   <p>- Spring 5.0+ 支持单构造函数的自动注入，无需显式注解</p>
 * <p/>
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final SysOptLogMapper sysOptLogMapper;

    @Override
    public void saveLog(SysOptLogEntity logEntity) {
        try {
            // 保存操作日志到数据库
            int result = sysOptLogMapper.insert(logEntity);
            if (result > 0) {
                log.debug("操作日志保存成功，日志ID: {}", logEntity.getId());
            } else {
                log.warn("操作日志保存失败，未影响任何行");
            }
        } catch (Exception e) {
            log.error("保存操作日志失败: {}", e.getMessage(), e);
        }
    }

    @Override
    @Async("logTaskExecutor")
    public void saveLogAsync(SysOptLogEntity logEntity) {
        saveLog(logEntity);
    }
}