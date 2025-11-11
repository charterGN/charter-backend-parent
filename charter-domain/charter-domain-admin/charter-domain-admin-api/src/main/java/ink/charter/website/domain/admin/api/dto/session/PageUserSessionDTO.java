package ink.charter.website.domain.admin.api.dto.session;

import ink.charter.website.common.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分页查询用户会话DTO
 *
 * @author charter
 * @create 2025/11/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "PageUserSessionDTO", description = "分页查询用户会话请求参数")
public class PageUserSessionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分页参数
     */
    @Schema(description = "分页参数")
    private PageRequest pageRequest = new PageRequest();

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 登录IP
     */
    @Schema(description = "登录IP")
    private String loginIp;

    /**
     * 状态（0离线 1在线）
     */
    @Schema(description = "状态（0离线 1在线）")
    private Integer status;

    /**
     * 登录时间查询开始时间
     */
    @Schema(description = "登录时间查询开始时间")
    private LocalDateTime loginStartTime;

    /**
     * 登录时间查询结束时间
     */
    @Schema(description = "登录时间查询结束时间")
    private LocalDateTime loginEndTime;
}
