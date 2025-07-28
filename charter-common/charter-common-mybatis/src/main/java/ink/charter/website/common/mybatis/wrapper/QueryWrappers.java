package ink.charter.website.common.mybatis.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

/**
 * 查询包装器工具类
 * 提供便捷的静态方法来创建各种查询包装器
 *
 * @author charter
 * @create 2025/07/19
 */
public final class QueryWrappers {

    private QueryWrappers() {
        // 工具类，禁止实例化
    }

    /**
     * 创建增强的Lambda查询包装器
     *
     * @param <T> 实体类型
     * @return 增强的Lambda查询包装器
     */
    public static <T> EnhancedLambdaQueryWrapper<T> lambdaQuery() {
        return EnhancedLambdaQueryWrapper.create();
    }

    /**
     * 创建增强的Lambda查询包装器
     *
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return 增强的Lambda查询包装器
     */
    public static <T> EnhancedLambdaQueryWrapper<T> lambdaQuery(Class<T> entityClass) {
        return EnhancedLambdaQueryWrapper.create(entityClass);
    }

    /**
     * 创建标准的Lambda查询包装器
     *
     * @param <T> 实体类型
     * @return 标准的Lambda查询包装器
     */
    public static <T> LambdaQueryWrapper<T> standardLambdaQuery() {
        return new LambdaQueryWrapper<>();
    }

    /**
     * 创建标准的Lambda查询包装器
     *
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return 标准的Lambda查询包装器
     */
    public static <T> LambdaQueryWrapper<T> standardLambdaQuery(Class<T> entityClass) {
        return new LambdaQueryWrapper<>(entityClass);
    }

    /**
     * 创建增强的Lambda更新包装器
     *
     * @param <T> 实体类型
     * @return 增强的Lambda更新包装器
     */
    public static <T> EnhancedLambdaUpdateWrapper<T> lambdaUpdate() {
        return EnhancedLambdaUpdateWrapper.create();
    }

    /**
     * 创建增强的Lambda更新包装器
     *
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return 增强的Lambda更新包装器
     */
    public static <T> EnhancedLambdaUpdateWrapper<T> lambdaUpdate(Class<T> entityClass) {
        return EnhancedLambdaUpdateWrapper.create(entityClass);
    }

    /**
     * 创建标准的Lambda更新包装器
     *
     * @param <T> 实体类型
     * @return 标准的Lambda更新包装器
     */
    public static <T> LambdaUpdateWrapper<T> standardLambdaUpdate() {
        return new LambdaUpdateWrapper<>();
    }

    /**
     * 创建标准的Lambda更新包装器
     *
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return 标准的Lambda更新包装器
     */
    public static <T> LambdaUpdateWrapper<T> standardLambdaUpdate(Class<T> entityClass) {
        return new LambdaUpdateWrapper<>(entityClass);
    }
}