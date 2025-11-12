package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysOptLogEntity;
import ink.charter.website.domain.admin.api.dto.optlog.PageOptLogDTO;
import ink.charter.website.domain.admin.api.repository.SysOptLogRepository;
import ink.charter.website.server.admin.sys.service.OptLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志服务实现类
 *
 * @author charter
 * @create 2025/11/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OptLogServiceImpl implements OptLogService {

    private final SysOptLogRepository sysOptLogRepository;

    @Override
    public PageResult<SysOptLogEntity> pageOptLogs(PageOptLogDTO pageRequest) {
        return sysOptLogRepository.pageOptLogs(pageRequest);
    }

    @Override
    public SysOptLogEntity getById(Long id) {
        return sysOptLogRepository.getById(id);
    }

    @Override
    public boolean create(SysOptLogEntity optLog) {
        return sysOptLogRepository.create(optLog);
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        return sysOptLogRepository.batchDelete(ids);
    }

    @Override
    public boolean deleteById(Long id) {
        return sysOptLogRepository.deleteById(id);
    }

    @Override
    public boolean truncate() {
        return sysOptLogRepository.truncate();
    }
}
