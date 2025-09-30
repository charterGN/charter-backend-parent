package ink.charter.website.common.core.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果类
 * 用于封装分页查询的返回数据
 *
 * @param <T> 数据类型
 * @author charter
 * @create 2025/09/30
 */
@Data
@Schema(name = "PageResult", description = "分页返回结果")
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 11406L;

    /**
     * 数据列表
     */
    @Schema(description = "数据列表")
    private List<T> records;

    /**
     * 总页数
     */
    @Schema(description = "总页数")
    private Integer total;

    /**
     * 构造函数
     */
    public PageResult() {
    }

    /**
     * 构造函数
     *
     * @param records 数据列表
     * @param total 总页数
     */
    public PageResult(List<T> records, Integer total) {
        this.records = records;
        this.total = total;
    }

    /**
     * 静态工厂方法 - 创建分页结果
     *
     * @param records 数据列表
     * @param total 总页数
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Integer total) {
        return new PageResult<>(records, total);
    }

    /**
     * 静态工厂方法 - 根据总记录数和页大小计算总页数
     *
     * @param records 数据列表
     * @param totalCount 总记录数
     * @param pageSize 每页数据大小
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> records, Long totalCount, Integer pageSize) {
        int total = (int) Math.ceil((double) totalCount / pageSize);
        return new PageResult<>(records, total);
    }

    /**
     * 静态工厂方法 - 创建空的分页结果
     *
     * @param <T> 数据类型
     * @return 空的分页结果
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(List.of(), 0);
    }

    /**
     * 判断是否有数据
     *
     * @return 是否有数据
     */
    public boolean hasData() {
        return records != null && !records.isEmpty();
    }

    /**
     * 获取数据数量
     *
     * @return 数据数量
     */
    public int getDataSize() {
        return records != null ? records.size() : 0;
    }
}