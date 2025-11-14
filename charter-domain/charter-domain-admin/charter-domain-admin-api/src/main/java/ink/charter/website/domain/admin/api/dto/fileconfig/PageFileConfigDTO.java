package ink.charter.website.domain.admin.api.dto.fileconfig;

import ink.charter.website.common.core.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询文件配置DTO
 *
 * @author charter
 * @create 2025/11/14
 */
@Data
@Schema(name = "PageFileConfigDTO", description = "分页查询文件配置请求")
public class PageFileConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "分页参数")
    private PageRequest pageRequest;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "是否启用（0否 1是）")
    private Integer enabled;

}
