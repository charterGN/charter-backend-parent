package ink.charter.website.domain.admin.api.vo.session;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会话统计信息VO
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@Schema(description = "会话统计信息")
public class SessionCountVO {

    @Schema(description = "用户ID", example = "1")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long userId;

    @Schema(description = "活跃会话数量", example = "3")
    private Integer activeSessionCount;

    /**
     * 静态工厂方法
     */
    public static SessionCountVO of(Long userId, Integer activeSessionCount) {
        SessionCountVO vo = new SessionCountVO();
        vo.setUserId(userId);
        vo.setActiveSessionCount(activeSessionCount);
        return vo;
    }
}