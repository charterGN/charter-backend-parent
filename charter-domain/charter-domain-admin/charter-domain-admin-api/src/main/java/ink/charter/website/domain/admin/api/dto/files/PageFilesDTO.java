package ink.charter.website.domain.admin.api.dto.files;

import ink.charter.website.common.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分页查询文件DTO
 *
 * @author charter
 * @create 2025/11/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "PageFilesDTO", description = "分页查询文件请求参数")
public class PageFilesDTO implements Serializable {

    @Schema(description = "分页参数")
    private PageRequest pageRequest;

    @Schema(description = "文件名称（模糊查询）")
    private String fileName;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "上传用户ID")
    private Long uploadUserId;

    @Schema(description = "文件状态（0不可用 1可用）")
    private Integer status;

    @Schema(description = "上传开始时间")
    private LocalDateTime uploadTimeStart;

    @Schema(description = "上传结束时间")
    private LocalDateTime uploadTimeEnd;

}
