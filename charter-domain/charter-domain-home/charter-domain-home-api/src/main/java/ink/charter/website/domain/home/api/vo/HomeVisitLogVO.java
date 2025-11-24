package ink.charter.website.domain.home.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访问日志VO
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@Schema(description = "访问日志")
public class HomeVisitLogVO {

    @Schema(description = "日志ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "访问IP")
    private String visitIp;

    @Schema(description = "访问地址")
    private String visitAddress;

    @Schema(description = "国家")
    private String visitCountry;

    @Schema(description = "省份")
    private String visitProvince;

    @Schema(description = "城市")
    private String visitCity;

    @Schema(description = "用户代理")
    private String userAgent;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "浏览器版本")
    private String browserVersion;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "设备类型")
    private String deviceType;

    @Schema(description = "来源页面")
    private String referer;

    @Schema(description = "访问页面")
    private String visitPage;

    @Schema(description = "访问时间")
    private LocalDateTime visitTime;

    @Schema(description = "停留时长（秒）")
    private Integer stayDuration;

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "是否新访客（0否 1是）")
    private Integer isNewVisitor;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
