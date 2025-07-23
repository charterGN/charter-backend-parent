package ink.charter.website.common.core.enums;

/**
 * 排序方向枚举
 * <p>
 * 用于数据查询时的排序控制，支持升序和降序
 * </p>
 *
 * @author Charter
 * @create 2025/07/19
 */
public enum SortDirectionEnum {
    
    /**
     * 升序
     */
    ASC("ASC", "升序", "从小到大排序"),
    
    /**
     * 降序
     */
    DESC("DESC", "降序", "从大到小排序");
    
    /**
     * 排序方向代码
     */
    private final String code;
    
    /**
     * 排序方向名称
     */
    private final String name;
    
    /**
     * 排序方向描述
     */
    private final String description;
    
    SortDirectionEnum(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取排序方向枚举
     *
     * @param code 排序方向代码
     * @return 对应的枚举，如果不存在则返回ASC
     */
    public static SortDirectionEnum getByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return ASC;
        }
        for (SortDirectionEnum direction : values()) {
            if (direction.getCode().equalsIgnoreCase(code.trim())) {
                return direction;
            }
        }
        return ASC;
    }
    
    /**
     * 判断是否为升序
     *
     * @return 是否为升序
     */
    public boolean isAsc() {
        return this == ASC;
    }
    
    /**
     * 判断是否为降序
     *
     * @return 是否为降序
     */
    public boolean isDesc() {
        return this == DESC;
    }
    
    /**
     * 获取相反的排序方向
     *
     * @return 相反的排序方向
     */
    public SortDirectionEnum reverse() {
        return this == ASC ? DESC : ASC;
    }
    
    /**
     * 转换为Spring Data的Sort.Direction
     * 注意：需要引入Spring Data依赖才能使用此方法
     *
     * @return Spring Data的Sort.Direction
     */
    public String toSpringDirection() {
        return this.code;
    }
}