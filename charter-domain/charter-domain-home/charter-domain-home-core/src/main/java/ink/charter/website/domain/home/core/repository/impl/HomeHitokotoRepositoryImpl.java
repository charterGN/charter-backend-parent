package ink.charter.website.domain.home.core.repository.impl;

import ink.charter.website.domain.home.api.entity.HomeHitokotoEntity;
import ink.charter.website.domain.home.api.repository.HomeHitokotoRepository;
import ink.charter.website.domain.home.core.repository.mapper.HomeHitokotoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 一言领域仓库实现
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeHitokotoRepositoryImpl implements HomeHitokotoRepository {

    private final HomeHitokotoMapper hitokotoMapper;

    @Override
    public List<HomeHitokotoEntity> getByConditions(List<String> types, Integer minLength, Integer maxLength) {
        return hitokotoMapper.selectByConditions(types, minLength, maxLength);
    }

    @Override
    public boolean incrementViewCount(Long id) {
        if (id == null) {
            return false;
        }
        try {
            return hitokotoMapper.incrementViewCount(id) > 0;
        } catch (Exception e) {
            log.error("增加一言展示次数失败: {}", id, e);
            return false;
        }
    }

    @Override
    public boolean incrementLikeCount(Long id) {
        if (id == null) {
            return false;
        }
        try {
            return hitokotoMapper.incrementLikeCount(id) > 0;
        } catch (Exception e) {
            log.error("增加一言点赞次数失败: {}", id, e);
            return false;
        }
    }
}
