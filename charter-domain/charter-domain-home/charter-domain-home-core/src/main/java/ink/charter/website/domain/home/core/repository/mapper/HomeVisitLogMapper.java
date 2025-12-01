package ink.charter.website.domain.home.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.home.api.entity.HomeVisitLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 访问日志Mapper
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper
public interface HomeVisitLogMapper extends BaseMapper<HomeVisitLogEntity> {

    /**
     * 更新停留时长
     *
     * @param id 日志ID
     * @param stayDuration 停留时长（秒）
     * @return 更新行数
     */
    default int updateStayDuration(Long id, Integer stayDuration) {
        if (id == null || stayDuration == null) {
            return 0;
        }
        return update(null, QueryWrappers.<HomeVisitLogEntity>lambdaUpdate()
            .set(HomeVisitLogEntity::getStayDuration, stayDuration)
            .eq(HomeVisitLogEntity::getId, id));
    }

    /**
     * 检查IP是否存在访问记录
     *
     * @param visitIp 访问IP
     * @return 是否存在
     */
    default boolean existsByIp(String visitIp) {
        if (visitIp == null || visitIp.isEmpty()) {
            return false;
        }
        return exists(QueryWrappers.<HomeVisitLogEntity>lambdaQuery()
            .eq(HomeVisitLogEntity::getVisitIp, visitIp)
            .eq(HomeVisitLogEntity::getIsDeleted, 0));
    }
}
