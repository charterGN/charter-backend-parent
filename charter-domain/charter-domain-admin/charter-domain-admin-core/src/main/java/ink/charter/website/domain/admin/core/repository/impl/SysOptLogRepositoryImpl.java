package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysOptLogEntity;
import ink.charter.website.common.log.mapper.SysOptLogMapper;
import ink.charter.website.domain.admin.api.dto.optlog.PageOptLogDTO;
import ink.charter.website.domain.admin.api.repository.SysOptLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作日志Repository实现
 *
 * @author charter
 * @create 2025/11/12
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysOptLogRepositoryImpl implements SysOptLogRepository {

    private final SysOptLogMapper optLogMapper;

    @Override
    public PageResult<SysOptLogEntity> pageOptLogs(PageOptLogDTO pageRequest) {
        IPage<SysOptLogEntity> result = optLogMapper.selectPage(
                pageRequest.getPageRequest(),
                pageRequest.getOptWebsite(),
                pageRequest.getOptModule(),
                pageRequest.getOptType(),
                pageRequest.getOptName(),
                pageRequest.getOptIp(),
                pageRequest.getOptStatus(),
                pageRequest.getOptStartTime(),
                pageRequest.getOptEndTime()
        );
        return PageResult.of(result.getRecords(), result.getTotal());
    }

    @Override
    public SysOptLogEntity getById(Long id) {
        return optLogMapper.selectById(id);
    }

    @Override
    public boolean create(SysOptLogEntity optLog) {
        return optLogMapper.insert(optLog) > 0;
    }

    @Override
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return optLogMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return optLogMapper.deleteById(id) > 0;
    }

    @Override
    public boolean truncate() {
        // 注意：truncate操作需要谨慎使用，这里使用delete all代替
        // 如果需要真正的truncate，需要在Mapper中定义SQL
        log.warn("执行清空操作日志表操作");
        return optLogMapper.delete(null) >= 0;
    }
}
