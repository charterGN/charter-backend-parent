package ink.charter.website.domain.home.core.repository.impl;

import ink.charter.website.domain.home.api.entity.HomeWallpaperEntity;
import ink.charter.website.domain.home.api.repository.HomeWallpaperRepository;
import ink.charter.website.domain.home.core.repository.mapper.HomeWallpaperMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 壁纸领域仓库实现
 *
 * @author charter
 * @create 2025/11/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeWallpaperRepositoryImpl implements HomeWallpaperRepository {

    private final HomeWallpaperMapper wallpaperMapper;

    @Override
    public List<HomeWallpaperEntity> getAllEnable(Integer wallpaperType) {
        return wallpaperMapper.getAllEnable(wallpaperType);
    }

    @Override
    public HomeWallpaperEntity getDefault() {
        return wallpaperMapper.selectDefault();
    }

    @Override
    public boolean incrementViewCount(Long id) {
        if (id == null) {
            return false;
        }
        try {
            return wallpaperMapper.incrementViewCount(id) > 0;
        } catch (Exception e) {
            log.error("增加壁纸浏览次数失败: {}", id, e);
            return false;
        }
    }

    @Override
    public boolean incrementDownloadCount(Long id) {
        if (id == null) {
            return false;
        }
        try {
            return wallpaperMapper.incrementDownloadCount(id) > 0;
        } catch (Exception e) {
            log.error("增加壁纸下载次数失败: {}", id, e);
            return false;
        }
    }
}
