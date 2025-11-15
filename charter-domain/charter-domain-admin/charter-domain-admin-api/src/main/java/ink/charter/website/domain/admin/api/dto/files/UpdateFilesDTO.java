package ink.charter.website.domain.admin.api.dto.files;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新文件DTO
 *
 * @author charter
 * @create 2025/11/15
 */
@Data
@Schema(name = "UpdateFilesDTO", description = "更新文件请求参数")
public class UpdateFilesDTO implements Serializable {

    @Schema(description = "文件ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件ID不能为空")
    private Long id;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件状态（0不可用 1可用）")
    private Integer status;

}
