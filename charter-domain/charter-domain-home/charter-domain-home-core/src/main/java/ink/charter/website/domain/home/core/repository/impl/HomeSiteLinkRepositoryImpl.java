package ink.charter.website.domain.home.core.repository.impl;

import ink.charter.website.domain.home.api.entity.HomeSiteLinkEntity;
import ink.charter.website.domain.home.api.repository.HomeSiteLinkRepository;
import ink.charter.website.domain.home.core.repository.mapper.HomeSiteLinkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 网站链接领域仓库实现
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeSiteLinkRepositoryImpl implements HomeSiteLinkRepository {

    private final HomeSiteLinkMapper siteLinkMapper;

    @Override
    public List<HomeSiteLinkEntity> getAllEnabled() {
        return siteLinkMapper.selectAllEnabled();
    }

    @Override
    public boolean incrementClickCount(Long id) {
        if (id == null) {
            return false;
        }
        try {
            return siteLinkMapper.incrementClickCount(id) > 0;
        } catch (Exception e) {
            log.error("增加网站链接点击次数失败: {}", id, e);
            return false;
        }
    }
}
