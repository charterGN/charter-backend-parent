package ink.charter.website.common.core.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;

/**
 * 分页请求参数类
 * 用于接收分页查询的基本参数
 *
 * @author charter
 * @create 2025/09/30
 */
@Data
@Schema(name = "PageRequest", description = "分页请求参数")
public class PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 11405L;

    /**
     * 页码，从1开始
     */
    @Schema(description = "页码，从1开始", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNo = 1;

    /**
     * 每页数据大小
     */
    @Schema(description = "每页数据大小", example = "10")
    @Min(value = 1, message = "每页数据大小不能小于1")
    @Max(value = 1000, message = "每页数据大小不能超过1000")
    private Integer pageSize = 10;

    /**
     * 构造函数
     */
    public PageRequest() {
    }

    /**
     * 构造函数
     *
     * @param pageNo  页码
     * @param pageSize 每页数据大小
     */
    public PageRequest(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo != null && pageNo > 0 ? pageNo : 1;
        this.pageSize = pageSize != null && pageSize > 0 ? pageSize : 10;
    }

    /**
     * 获取偏移量（用于数据库查询）
     *
     * @return 偏移量
     */
    public Integer getOffset() {
        return (pageNo - 1) * pageSize;
    }

    /**
     * 获取限制数量（用于数据库查询）
     *
     * @return 限制数量
     */
    public Integer getLimit() {
        return pageSize;
    }
}