package ink.charter.website.server.home.service.impl;

import ink.charter.website.domain.home.api.entity.HomeSiteLinkEntity;
import ink.charter.website.domain.home.api.repository.HomeSiteLinkRepository;
import ink.charter.website.server.home.service.HomeSiteLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网站链接服务实现类
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeSiteLinkServiceImpl implements HomeSiteLinkService {

    private final HomeSiteLinkRepository siteLinkRepository;

    @Override
    public List<HomeSiteLinkEntity> getAllEnabled() {
        return siteLinkRepository.getAllEnabled();
    }

    @Override
    public boolean incrementClickCount(Long id) {
        return siteLinkRepository.incrementClickCount(id);
    }
}
