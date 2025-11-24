package ink.charter.website.server.home.service.impl;

import ink.charter.website.domain.home.api.entity.HomeSiteConfigEntity;
import ink.charter.website.domain.home.api.repository.HomeSiteConfigRepository;
import ink.charter.website.server.home.service.HomeSiteConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 站点配置服务实现类
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeSiteConfigServiceImpl implements HomeSiteConfigService {

    private final HomeSiteConfigRepository siteConfigRepository;

    @Override
    public List<HomeSiteConfigEntity> getAllEnabled() {
        return siteConfigRepository.getAllEnabled();
    }

    @Override
    public List<HomeSiteConfigEntity> getByConfigType(String configType) {
        return siteConfigRepository.getByConfigType(configType);
    }
}
