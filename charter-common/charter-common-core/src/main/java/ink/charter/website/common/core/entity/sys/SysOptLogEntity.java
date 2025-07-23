package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 系统操作日志实体类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_opt_log")
@Schema(name = "SysOptLogEntity", description = "系统操作日志")
public class SysOptLogEntity extends BaseEntity {

    @Schema(description = "操作网站")
    private String optWebsite;

    @Schema(description = "操作模块")
    private String optModule;

    @Schema(description = "操作类型")
    private String optType;

    @Schema(description = "操作url")
    private String optUrl;

    @Schema(description = "操作方法")
    private String optMethod;

    @Schema(description = "操作描述")
    private String optDesc;

    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "请求参数")
    private String requestParam;

    @Schema(description = "返回数据")
    private String responseData;

    @Schema(description = "操作人id")
    private Long optId;

    @Schema(description = "操作人名称")
    private String optName;

    @Schema(description = "操作ip")
    private String optIp;

    @Schema(description = "操作地址")
    private String optAddress;

    @Schema(description = "操作状态（0正常 1异常）")
    private Integer optStatus;

    @Schema(description = "异常信息")
    private String errorMsg;

    @Schema(description = "操作耗时")
    private Long optCostTime;
}
