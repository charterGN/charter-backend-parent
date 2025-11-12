package ink.charter.website.domain.admin.api.dto.optlog;

import ink.charter.website.common.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分页查询操作日志DTO
 *
 * @author charter
 * @create 2025/11/12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "PageOptLogDTO", description = "分页查询操作日志请求参数")
public class PageOptLogDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分页参数
     */
    @Schema(description = "分页参数")
    private PageRequest pageRequest = new PageRequest();

    /**
     * 操作网站
     */
    @Schema(description = "操作网站")
    private String optWebsite;

    /**
     * 操作模块
     */
    @Schema(description = "操作模块")
    private String optModule;

    /**
     * 操作类型
     */
    @Schema(description = "操作类型")
    private String optType;

    /**
     * 操作人名称
     */
    @Schema(description = "操作人名称")
    private String optName;

    /**
     * 操作IP
     */
    @Schema(description = "操作IP")
    private String optIp;

    /**
     * 操作状态（0正常 1异常）
     */
    @Schema(description = "操作状态（0正常 1异常）")
    private Integer optStatus;

    /**
     * 操作时间查询开始时间
     */
    @Schema(description = "操作时间查询开始时间")
    private LocalDateTime optStartTime;

    /**
     * 操作时间查询结束时间
     */
    @Schema(description = "操作时间查询结束时间")
    private LocalDateTime optEndTime;
}
