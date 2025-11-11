package ink.charter.website.domain.admin.api.dto.dict;

import ink.charter.website.common.core.common.PageRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分页查询字典类型请求参数
 *
 * @author: Zhoutf
 * @data: 2025/11/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "PageDictTypeDTO", description = "分页查询字典类型请求参数")
public class PageDictTypeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分页参数
     */
    @Schema(description = "分页参数")
    private PageRequest pageRequest = new PageRequest();

    /**
     * 字典名称
     */
    @Schema(description = "字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    private String dictType;

    /**
     * 状态（0禁用 1启用）
     */
    @Schema(description = "状态（0禁用 1启用）")
    private Integer status;
}
