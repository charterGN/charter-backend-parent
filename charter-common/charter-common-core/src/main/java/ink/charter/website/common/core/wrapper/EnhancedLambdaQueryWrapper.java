package ink.charter.website.common.core.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * LambdaQueryWrapper增强类
 * 提供更便捷的条件构建方法，支持条件判断后再添加查询条件
 *
 * @author charter
 * @create 2025/07/19
 */
public class EnhancedLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {

    /**
     * 创建增强查询包装器
     *
     * @param <T> 实体类型
     * @return 增强查询包装器
     */
    public static <T> EnhancedLambdaQueryWrapper<T> create() {
        return new EnhancedLambdaQueryWrapper<>();
    }

    /**
     * 创建增强查询包装器
     *
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return 增强查询包装器
     */
    public static <T> EnhancedLambdaQueryWrapper<T> create(Class<T> entityClass) {
        return new EnhancedLambdaQueryWrapper<>();
    }

    // ==================== 字符串相关条件 ====================

    /**
     * 等于条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        if (Objects.nonNull(val)) {
            if (val instanceof String && StringUtils.hasText((String) val)) {
                super.eq(column, val);
            } else if (!(val instanceof String)) {
                super.eq(column, val);
            }
        }
        return this;
    }

    /**
     * 不等于条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> neIfPresent(SFunction<T, ?> column, Object val) {
        if (Objects.nonNull(val)) {
            if (val instanceof String && StringUtils.hasText((String) val)) {
                super.ne(column, val);
            } else if (!(val instanceof String)) {
                super.ne(column, val);
            }
        }
        return this;
    }

    /**
     * 模糊查询条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> likeIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            super.like(column, val);
        }
        return this;
    }

    /**
     * 左模糊查询条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> likeLeftIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            super.likeLeft(column, val);
        }
        return this;
    }

    /**
     * 右模糊查询条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> likeRightIfPresent(SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val)) {
            super.likeRight(column, val);
        }
        return this;
    }

    // ==================== 数值比较条件 ====================

    /**
     * 大于条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        if (Objects.nonNull(val)) {
            super.gt(column, val);
        }
        return this;
    }

    /**
     * 大于等于条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> geIfPresent(SFunction<T, ?> column, Object val) {
        if (Objects.nonNull(val)) {
            super.ge(column, val);
        }
        return this;
    }

    /**
     * 小于条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        if (Objects.nonNull(val)) {
            super.lt(column, val);
        }
        return this;
    }

    /**
     * 小于等于条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val    值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> leIfPresent(SFunction<T, ?> column, Object val) {
        if (Objects.nonNull(val)) {
            super.le(column, val);
        }
        return this;
    }

    /**
     * 区间查询条件 - 当值不为空时添加
     *
     * @param column 字段
     * @param val1   起始值
     * @param val2   结束值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (Objects.nonNull(val1) && Objects.nonNull(val2)) {
            super.between(column, val1, val2);
        }
        return this;
    }

    // ==================== 集合条件 ====================

    /**
     * IN条件 - 当集合不为空时添加
     *
     * @param column 字段
     * @param coll   集合
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> inIfPresent(SFunction<T, ?> column, Collection<?> coll) {
        if (Objects.nonNull(coll) && !coll.isEmpty()) {
            super.in(column, coll);
        }
        return this;
    }

    /**
     * NOT IN条件 - 当集合不为空时添加
     *
     * @param column 字段
     * @param coll   集合
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> notInIfPresent(SFunction<T, ?> column, Collection<?> coll) {
        if (Objects.nonNull(coll) && !coll.isEmpty()) {
            super.notIn(column, coll);
        }
        return this;
    }

    // ==================== 自定义条件判断 ====================

    /**
     * 自定义条件判断 - 当条件为真时添加等于条件
     *
     * @param condition 条件
     * @param column    字段
     * @param val       值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> eqIf(boolean condition, SFunction<T, ?> column, Object val) {
        if (condition) {
            super.eq(column, val);
        }
        return this;
    }

    /**
     * 自定义条件判断 - 当条件为真时添加模糊查询条件
     *
     * @param condition 条件
     * @param column    字段
     * @param val       值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> likeIf(boolean condition, SFunction<T, ?> column, String val) {
        if (condition) {
            super.like(column, val);
        }
        return this;
    }

    /**
     * 自定义条件判断 - 当条件为真时添加IN条件
     *
     * @param condition 条件
     * @param column    字段
     * @param coll      集合
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> inIf(boolean condition, SFunction<T, ?> column, Collection<?> coll) {
        if (condition) {
            super.in(column, coll);
        }
        return this;
    }

    // ==================== 高级条件判断 ====================

    /**
     * 使用Predicate进行条件判断
     *
     * @param predicate 条件判断器
     * @param column    字段
     * @param val       值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> eqIfMatch(Predicate<Object> predicate, SFunction<T, ?> column, Object val) {
        if (Objects.nonNull(val) && predicate.test(val)) {
            super.eq(column, val);
        }
        return this;
    }

    /**
     * 使用Predicate进行条件判断 - 模糊查询
     *
     * @param predicate 条件判断器
     * @param column    字段
     * @param val       值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> likeIfMatch(Predicate<String> predicate, SFunction<T, ?> column, String val) {
        if (StringUtils.hasText(val) && predicate.test(val)) {
            super.like(column, val);
        }
        return this;
    }

    // ==================== 便捷方法 ====================

    /**
     * 添加状态等于条件（通常用于查询启用状态的数据）
     *
     * @param column 状态字段
     * @param status 状态值
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> enabledStatus(SFunction<T, ?> column, Integer status) {
        if (Objects.nonNull(status)) {
            super.eq(column, status);
        }
        return this;
    }

    /**
     * 添加逻辑删除条件（查询未删除的数据）
     * 注意：如果实体使用了@TableLogic注解，MyBatis-Plus会自动处理，无需手动添加此条件
     *
     * @param column 逻辑删除字段
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> notDeleted(SFunction<T, ?> column) {
        super.eq(column, false);
        return this;
    }

    /**
     * 添加时间范围查询条件
     *
     * @param column    时间字段
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return this
     */
    public EnhancedLambdaQueryWrapper<T> timeRangeIfPresent(SFunction<T, ?> column, Object startTime, Object endTime) {
        if (Objects.nonNull(startTime)) {
            super.ge(column, startTime);
        }
        if (Objects.nonNull(endTime)) {
            super.le(column, endTime);
        }
        return this;
    }

    /**
     * 链式调用 - 返回增强类型以支持链式调用
     */
    @Override
    public EnhancedLambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object val) {
        super.eq(column, val);
        return this;
    }

    @Override
    public EnhancedLambdaQueryWrapper<T> ne(SFunction<T, ?> column, Object val) {
        super.ne(column, val);
        return this;
    }

    @Override
    public EnhancedLambdaQueryWrapper<T> like(SFunction<T, ?> column, Object val) {
        super.like(column, val);
        return this;
    }

    @Override
    public EnhancedLambdaQueryWrapper<T> orderByAsc(SFunction<T, ?> column) {
        super.orderByAsc(column);
        return this;
    }

    @Override
    public EnhancedLambdaQueryWrapper<T> orderByDesc(SFunction<T, ?> column) {
        super.orderByDesc(column);
        return this;
    }
}