package ink.charter.website.domain.admin.api.dto.files;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建文件DTO
 *
 * @author charter
 * @create 2025/11/15
 */
@Data
@Schema(name = "CreateFilesDTO", description = "创建文件请求参数")
public class CreateFilesDTO implements Serializable {

    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    @Schema(description = "文件大小", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

    @Schema(description = "文件MD5值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文件MD5值不能为空")
    private String fileMd5;

    @Schema(description = "文件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "上传用户ID")
    private Long uploadUserId;

}
