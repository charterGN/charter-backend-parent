package ink.charter.website.common.core.enums;

/**
 * 通用状态枚举
 * <p>
 * 用于表示各种业务实体的状态，如启用/禁用、正常/异常等
 * </p>
 *
 * @author Charter
 * @create 2025/07/19
 */
public enum StatusEnum {
    
    /**
     * 启用状态
     */
    ENABLED(1, "启用"),
    
    /**
     * 禁用状态
     */
    DISABLED(0, "禁用");
    
    /**
     * 状态码
     */
    private final Integer code;
    
    /**
     * 状态描述
     */
    private final String description;
    
    StatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 对应的枚举，如果不存在则返回null
     */
    public static StatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (StatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 判断是否为启用状态
     *
     * @return 是否启用
     */
    public boolean isEnabled() {
        return this == ENABLED;
    }
    
    /**
     * 判断是否为禁用状态
     *
     * @return 是否禁用
     */
    public boolean isDisabled() {
        return this == DISABLED;
    }
}