package ink.charter.website.domain.admin.api.vo.optlog;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志信息VO
 *
 * @author charter
 * @create 2025/11/12
 */
@Data
@Schema(description = "操作日志信息")
public class OptLogVO {

    @Schema(description = "日志ID", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "操作网站", example = "后台管理系统")
    private String optWebsite;

    @Schema(description = "操作模块", example = "用户管理")
    private String optModule;

    @Schema(description = "操作类型", example = "新增")
    private String optType;

    @Schema(description = "操作url", example = "/api/user/create")
    private String optUrl;

    @Schema(description = "操作方法", example = "createUser")
    private String optMethod;

    @Schema(description = "操作描述", example = "创建用户")
    private String optDesc;

    @Schema(description = "请求方式", example = "POST")
    private String requestMethod;

    @Schema(description = "请求参数")
    private String requestParam;

    @Schema(description = "返回数据")
    private String responseData;

    @Schema(description = "操作人id", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long optId;

    @Schema(description = "操作人名称", example = "admin")
    private String optName;

    @Schema(description = "操作ip", example = "192.168.1.1")
    private String optIp;

    @Schema(description = "操作地址", example = "内网")
    private String optAddress;

    @Schema(description = "操作状态（0正常 1异常）", example = "0")
    private Integer optStatus;

    @Schema(description = "异常信息")
    private String errorMsg;

    @Schema(description = "操作耗时（毫秒）", example = "100")
    private Long optCostTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
