package ink.charter.website.domain.home.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 门户访问日志实体类
 *
 * @author charter
 * @create 2025/11/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("home_visit_log")
@Schema(name = "HomeVisitLogEntity", description = "门户访问日志")
public class HomeVisitLogEntity extends BaseEntity {

    @Schema(description = "访问IP")
    private String visitIp;

    @Schema(description = "访问地址（IP解析）")
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

    @Schema(description = "设备类型（PC/Mobile/Tablet）")
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
}
