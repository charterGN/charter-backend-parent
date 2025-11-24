package ink.charter.website.server.home.service.impl;

import ink.charter.website.domain.home.api.entity.HomeWallpaperEntity;
import ink.charter.website.domain.home.api.repository.HomeWallpaperRepository;
import ink.charter.website.server.home.service.HomeWallpaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 壁纸服务实现类
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeWallpaperServiceImpl implements HomeWallpaperService {

    private final HomeWallpaperRepository wallpaperRepository;
    private final Random random = new Random();

    @Override
    public HomeWallpaperEntity getRandom(Integer wallpaperType) {
        List<HomeWallpaperEntity> records = wallpaperRepository.getAllEnable(wallpaperType);

        if (records.isEmpty()) {
            return null;
        }
        
        // 随机返回一张
        return records.get(random.nextInt(records.size()));
    }

    @Override
    public HomeWallpaperEntity getDefault() {
        return wallpaperRepository.getDefault();
    }

    @Override
    public boolean incrementViewCount(Long id) {
        return wallpaperRepository.incrementViewCount(id);
    }

    @Override
    public boolean incrementDownloadCount(Long id) {
        return wallpaperRepository.incrementDownloadCount(id);
    }
}
