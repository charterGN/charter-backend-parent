package ink.charter.website.domain.home.core.repository.impl;

import ink.charter.website.domain.home.api.entity.HomeVisitLogEntity;
import ink.charter.website.domain.home.api.repository.HomeVisitLogRepository;
import ink.charter.website.domain.home.core.repository.mapper.HomeVisitLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 访问日志领域仓库实现
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeVisitLogRepositoryImpl implements HomeVisitLogRepository {

    private final HomeVisitLogMapper visitLogMapper;

    @Override
    public boolean create(HomeVisitLogEntity entity) {
        if (entity == null) {
            return false;
        }
        try {
            return visitLogMapper.insert(entity) > 0;
        } catch (Exception e) {
            log.error("创建访问日志失败", e);
            return false;
        }
    }

    @Override
    public boolean updateStayDuration(Long id, Integer stayDuration) {
        if (id == null || stayDuration == null) {
            return false;
        }
        try {
            return visitLogMapper.updateStayDuration(id, stayDuration) > 0;
        } catch (Exception e) {
            log.error("更新访问停留时长失败: {}", id, e);
            return false;
        }
    }

    @Override
    public boolean existsByIp(String visitIp) {
        if (visitIp == null || visitIp.isEmpty()) {
            return false;
        }
        try {
            return visitLogMapper.existsByIp(visitIp);
        } catch (Exception e) {
            log.error("检查IP是否存在失败: {}", visitIp, e);
            return false;
        }
    }
}
