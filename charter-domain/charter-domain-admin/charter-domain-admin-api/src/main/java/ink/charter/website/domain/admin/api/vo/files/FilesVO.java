package ink.charter.website.domain.admin.api.vo.files;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件信息VO
 *
 * @author charter
 * @create 2025/11/15
 */
@Data
@Schema(name = "FilesVO", description = "文件信息")
public class FilesVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "文件ID")
    @JsonSerialize(using = ToStringSerializer.class) // 将Long类型序列化为String，避免前端精度丢失
    private Long id;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件MD5值")
    private String fileMd5;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "上传用户ID")
    private Long uploadUserId;

    @Schema(description = "上传时间")
    private LocalDateTime uploadTime;

    @Schema(description = "最后一次访问时间")
    private LocalDateTime lastAccessTime;

    @Schema(description = "文件状态（0不可用 1可用）")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
