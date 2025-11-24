package ink.charter.website.domain.home.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建访问日志DTO
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@Schema(description = "创建访问日志DTO")
public class CreateVisitLogDTO {

    @Schema(description = "访问IP")
    private String visitIp;

    @Schema(description = "用户代理")
    private String userAgent;

    @Schema(description = "来源页面")
    private String referer;

    @Schema(description = "访问页面")
    private String visitPage;

    @Schema(description = "会话ID")
    private String sessionId;
}
