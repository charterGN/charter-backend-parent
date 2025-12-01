package ink.charter.website.domain.home.api.repository;

import ink.charter.website.domain.home.api.entity.HomeVisitLogEntity;

/**
 * 访问日志领域仓库
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeVisitLogRepository {

    /**
     * 创建访问日志
     *
     * @param entity 访问日志实体
     * @return 是否成功
     */
    boolean create(HomeVisitLogEntity entity);

    /**
     * 更新停留时长
     *
     * @param id 日志ID
     * @param stayDuration 停留时长（秒）
     * @return 是否成功
     */
    boolean updateStayDuration(Long id, Integer stayDuration);

    /**
     * 检查IP是否存在访问记录
     *
     * @param visitIp 访问IP
     * @return 是否存在
     */
    boolean existsByIp(String visitIp);
}
