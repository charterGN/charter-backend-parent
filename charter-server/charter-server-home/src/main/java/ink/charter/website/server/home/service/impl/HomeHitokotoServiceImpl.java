package ink.charter.website.server.home.service.impl;

import ink.charter.website.domain.home.api.entity.HomeHitokotoEntity;
import ink.charter.website.domain.home.api.repository.HomeHitokotoRepository;
import ink.charter.website.server.home.service.HomeHitokotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 一言服务实现类
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeHitokotoServiceImpl implements HomeHitokotoService {

    private final HomeHitokotoRepository hitokotoRepository;
    private final Random random = new Random();

    @Override
    public HomeHitokotoEntity getRandom(List<String> types, Integer minLength, Integer maxLength) {
        List<HomeHitokotoEntity> list = hitokotoRepository.getByConditions(types, minLength, maxLength);
        
        if (list.isEmpty()) {
            return null;
        }
        
        // 随机返回一条
        return list.get(random.nextInt(list.size()));
    }

    @Override
    public boolean incrementViewCount(Long id) {
        return hitokotoRepository.incrementViewCount(id);
    }

    @Override
    public boolean incrementLikeCount(Long id) {
        return hitokotoRepository.incrementLikeCount(id);
    }
}
