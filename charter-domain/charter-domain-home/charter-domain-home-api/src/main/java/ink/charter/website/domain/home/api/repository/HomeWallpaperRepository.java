package ink.charter.website.domain.home.api.repository;

import ink.charter.website.domain.home.api.entity.HomeWallpaperEntity;
import java.util.List;

/**
 * 壁纸领域仓库
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeWallpaperRepository {

    /**
     * 获取所有启用壁纸
     *
     * @param wallpaperType 壁纸类型
     * @return 壁纸实体列表
     */
    List<HomeWallpaperEntity> getAllEnable(Integer wallpaperType);

    /**
     * 获取默认壁纸
     *
     * @return 壁纸实体
     */
    HomeWallpaperEntity getDefault();

    /**
     * 增加浏览次数
     *
     * @param id 壁纸ID
     * @return 是否成功
     */
    boolean incrementViewCount(Long id);

    /**
     * 增加下载次数
     *
     * @param id 壁纸ID
     * @return 是否成功
     */
    boolean incrementDownloadCount(Long id);
}
