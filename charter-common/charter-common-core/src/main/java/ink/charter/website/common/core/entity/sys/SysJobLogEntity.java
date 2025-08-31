package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 系统定时任务执行日志实体类
 *
 * @author charter
 * @create 2025/07/30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_job_log")
@Schema(name = "SysJobLogEntity", description = "系统定时任务执行日志")
public class SysJobLogEntity extends BaseEntity {

    @Schema(description = "任务ID")
    private Long jobId;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务分组")
    private String jobGroup;

    @Schema(description = "执行时间")
    private LocalDateTime executeTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "执行状态（0失败 1成功 2执行中）")
    private Integer executeStatus;

    @Schema(description = "耗时（毫秒）")
    private Long costTime;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "执行服务器IP")
    private String serverIp;
}