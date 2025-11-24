package ink.charter.website.server.home.service;

import ink.charter.website.domain.home.api.dto.CreateVisitLogDTO;
import ink.charter.website.domain.home.api.entity.HomeVisitLogEntity;

/**
 * 访问日志服务接口
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeVisitLogService {

    /**
     * 创建访问日志
     *
     * @param dto 访问日志DTO
     * @return 日志ID
     */
    Long create(CreateVisitLogDTO dto);

    /**
     * 更新停留时长
     *
     * @param id 日志ID
     * @param stayDuration 停留时长（秒）
     * @return 是否成功
     */
    boolean updateStayDuration(Long id, Integer stayDuration);
}
