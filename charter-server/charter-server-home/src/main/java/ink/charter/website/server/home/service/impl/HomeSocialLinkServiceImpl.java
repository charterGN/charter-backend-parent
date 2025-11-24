package ink.charter.website.server.home.service.impl;

import ink.charter.website.domain.home.api.entity.HomeSocialLinkEntity;
import ink.charter.website.domain.home.api.repository.HomeSocialLinkRepository;
import ink.charter.website.server.home.service.HomeSocialLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 社交链接服务实现类
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeSocialLinkServiceImpl implements HomeSocialLinkService {

    private final HomeSocialLinkRepository socialLinkRepository;

    @Override
    public List<HomeSocialLinkEntity> getAllEnabled() {
        return socialLinkRepository.getAllEnabled();
    }
}
