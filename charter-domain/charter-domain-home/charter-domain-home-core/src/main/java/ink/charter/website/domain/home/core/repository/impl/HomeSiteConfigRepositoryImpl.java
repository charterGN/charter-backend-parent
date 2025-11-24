package ink.charter.website.domain.home.core.repository.impl;

import ink.charter.website.domain.home.api.entity.HomeSiteConfigEntity;
import ink.charter.website.domain.home.api.repository.HomeSiteConfigRepository;
import ink.charter.website.domain.home.core.repository.mapper.HomeSiteConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 站点配置领域仓库实现
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeSiteConfigRepositoryImpl implements HomeSiteConfigRepository {

    private final HomeSiteConfigMapper siteConfigMapper;

    @Override
    public List<HomeSiteConfigEntity> getAllEnabled() {
        return siteConfigMapper.selectAllEnabled();
    }

    @Override
    public List<HomeSiteConfigEntity> getByConfigType(String configType) {
        return siteConfigMapper.selectByConfigType(configType);
    }
}
