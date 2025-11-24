package ink.charter.website.domain.home.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.home.api.entity.HomeHitokotoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 一言Mapper
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper
public interface HomeHitokotoMapper extends BaseMapper<HomeHitokotoEntity> {

    /**
     * 根据条件查询一言列表（用于随机）
     *
     * @param types 一言类型列表
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 一言列表
     */
    default List<HomeHitokotoEntity> selectByConditions(List<String> types, Integer minLength, Integer maxLength) {
        return selectList(QueryWrappers.<HomeHitokotoEntity>lambdaQuery()
            .inIfPresent(HomeHitokotoEntity::getType, types)
            .geIfPresent(HomeHitokotoEntity::getLength, minLength)
            .leIfPresent(HomeHitokotoEntity::getLength, maxLength)
            .orderByDesc(HomeHitokotoEntity::getCreateTime));
    }

    /**
     * 增加展示次数
     *
     * @param id 一言ID
     * @return 更新行数
     */
    default int incrementViewCount(Long id) {
        if (id == null) {
            return 0;
        }
        return update(null, QueryWrappers.<HomeHitokotoEntity>lambdaUpdate()
            .setSql("view_count = view_count + 1")
            .eq(HomeHitokotoEntity::getId, id));
    }

    /**
     * 增加点赞次数
     *
     * @param id 一言ID
     * @return 更新行数
     */
    default int incrementLikeCount(Long id) {
        if (id == null) {
            return 0;
        }
        return update(null, QueryWrappers.<HomeHitokotoEntity>lambdaUpdate()
            .setSql("like_count = like_count + 1")
            .eq(HomeHitokotoEntity::getId, id));
    }
}
