package ink.charter.website.domain.admin.api.dto.user;

import ink.charter.website.common.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分页查询用户DTO
 *
 * @author: Zhoutf
 * @data: 2025/9/30
 */
@Data
@Schema(description = "分页查询用户DTO")
public class PageUserDTO {

    @Schema(description = "分页参数")
    private PageRequest pageRequest;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "登录时间查询开始时间")
    private LocalDateTime loginStartTime;

    @Schema(description = "登录时间查询结束时间")
    private LocalDateTime loginEndTime;
}
