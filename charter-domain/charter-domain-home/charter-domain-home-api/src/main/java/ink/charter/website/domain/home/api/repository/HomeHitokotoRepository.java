package ink.charter.website.domain.home.api.repository;

import ink.charter.website.domain.home.api.entity.HomeHitokotoEntity;

import java.util.List;

/**
 * 一言领域仓库
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeHitokotoRepository {

    /**
     * 根据条件查询一言列表
     *
     * @param types 一言类型列表
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 一言列表
     */
    List<HomeHitokotoEntity> getByConditions(List<String> types, Integer minLength, Integer maxLength);

    /**
     * 增加展示次数
     *
     * @param id 一言ID
     * @return 是否成功
     */
    boolean incrementViewCount(Long id);

    /**
     * 增加点赞次数
     *
     * @param id 一言ID
     * @return 是否成功
     */
    boolean incrementLikeCount(Long id);
}
