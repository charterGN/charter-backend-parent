package ink.charter.website.common.core.enums;

/**
 * 性别枚举
 * <p>
 * 用于表示用户性别信息
 * </p>
 *
 * @author Charter
 * @create 2025/07/19
 */
public enum GenderEnum {
    
    /**
     * 男性
     */
    MALE(1, "男", "M"),
    
    /**
     * 女性
     */
    FEMALE(2, "女", "F"),
    
    /**
     * 未知/不愿透露
     */
    UNKNOWN(0, "未知", "U");
    
    /**
     * 性别代码
     */
    private final Integer code;
    
    /**
     * 性别名称
     */
    private final String name;
    
    /**
     * 性别标识
     */
    private final String symbol;
    
    GenderEnum(Integer code, String name, String symbol) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * 根据代码获取性别枚举
     *
     * @param code 性别代码
     * @return 对应的枚举，如果不存在则返回UNKNOWN
     */
    public static GenderEnum getByCode(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (GenderEnum gender : values()) {
            if (gender.getCode().equals(code)) {
                return gender;
            }
        }
        return UNKNOWN;
    }
    
    /**
     * 根据性别标识获取性别枚举
     *
     * @param symbol 性别标识
     * @return 对应的枚举，如果不存在则返回UNKNOWN
     */
    public static GenderEnum getBySymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            return UNKNOWN;
        }
        for (GenderEnum gender : values()) {
            if (gender.getSymbol().equalsIgnoreCase(symbol.trim())) {
                return gender;
            }
        }
        return UNKNOWN;
    }
    
    /**
     * 判断是否为男性
     *
     * @return 是否为男性
     */
    public boolean isMale() {
        return this == MALE;
    }
    
    /**
     * 判断是否为女性
     *
     * @return 是否为女性
     */
    public boolean isFemale() {
        return this == FEMALE;
    }
    
    /**
     * 判断是否为未知性别
     *
     * @return 是否为未知
     */
    public boolean isUnknown() {
        return this == UNKNOWN;
    }
}