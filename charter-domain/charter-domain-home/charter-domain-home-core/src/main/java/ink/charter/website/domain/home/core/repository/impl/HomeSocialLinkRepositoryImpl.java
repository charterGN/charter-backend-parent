package ink.charter.website.domain.home.core.repository.impl;

import ink.charter.website.domain.home.api.entity.HomeSocialLinkEntity;
import ink.charter.website.domain.home.api.repository.HomeSocialLinkRepository;
import ink.charter.website.domain.home.core.repository.mapper.HomeSocialLinkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 社交链接领域仓库实现
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeSocialLinkRepositoryImpl implements HomeSocialLinkRepository {

    private final HomeSocialLinkMapper socialLinkMapper;

    @Override
    public List<HomeSocialLinkEntity> getAllEnabled() {
        return socialLinkMapper.selectAllEnabled();
    }
}
