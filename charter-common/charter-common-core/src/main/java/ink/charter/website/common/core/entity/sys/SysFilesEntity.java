package ink.charter.website.common.core.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import ink.charter.website.common.core.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 文件信息实体类
 *
 * @author charter
 * @create 2025/07/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_files")
@Schema(name = "SysFilesEntity", description = "文件信息")
public class SysFilesEntity extends BaseEntity {

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

}