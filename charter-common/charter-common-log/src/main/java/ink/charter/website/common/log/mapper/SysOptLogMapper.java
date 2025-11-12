package ink.charter.website.common.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.common.core.common.PageRequest;
import ink.charter.website.common.core.entity.sys.SysOptLogEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

/**
 * 系统操作日志Mapper接口
 *
 * @author charter
 * @create 2025/07/17
 */
@Mapper
public interface SysOptLogMapper extends BaseMapper<SysOptLogEntity> {
    
    /**
     * 分页查询操作日志
     *
     * @param page 分页参数
     * @param optWebsite 操作网站
     * @param optModule 操作模块
     * @param optType 操作类型
     * @param optName 操作名称
     * @param optIp 操作IP
     * @param optStatus 操作状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分页结果
     */
    default IPage<SysOptLogEntity> selectPage(PageRequest page, String optWebsite,
                                              String optModule, String optType,
                                              String optName, String optIp,
                                              Integer optStatus, LocalDateTime startTime, LocalDateTime endTime) {
        return selectPage(new Page<>(page.getPageNo(), page.getPageSize()), QueryWrappers.<SysOptLogEntity>lambdaQuery()
                .likeIfPresent(SysOptLogEntity::getOptWebsite, optWebsite)
                .likeIfPresent(SysOptLogEntity::getOptModule, optModule)
                .likeIfPresent(SysOptLogEntity::getOptType, optType)
                .likeIfPresent(SysOptLogEntity::getOptName, optName)
                .likeIfPresent(SysOptLogEntity::getOptIp, optIp)
                .eqIfPresent(SysOptLogEntity::getOptStatus, optStatus)
                .betweenIfPresent(SysOptLogEntity::getCreateTime, startTime, endTime)
                .orderByDesc(SysOptLogEntity::getCreateTime));
    }
}