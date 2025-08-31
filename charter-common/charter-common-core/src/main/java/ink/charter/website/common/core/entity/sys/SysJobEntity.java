package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统定时任务实体类
 *
 * @author charter
 * @create 2025/07/30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_job")
@Schema(name = "SysJobEntity", description = "系统定时任务")
public class SysJobEntity extends BaseEntity {

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务分组")
    private String jobGroup;

    @Schema(description = "任务执行类")
    private String jobClass;

    @Schema(description = "Cron表达式")
    private String cronExpression;

    @Schema(description = "任务描述")
    private String description;

    @Schema(description = "状态（0暂停 1启用）")
    private Integer status;

    @Schema(description = "是否并发（0禁止 1允许）")
    private Integer concurrent;

    @Schema(description = "错过执行策略（1立即执行 2执行一次 3放弃执行）")
    private Integer misfirePolicy;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "超时时间（秒）")
    private Integer timeout;

    @Schema(description = "任务参数（JSON格式）")
    private String params;
}