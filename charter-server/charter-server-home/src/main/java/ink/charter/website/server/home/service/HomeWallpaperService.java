package ink.charter.website.server.home.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.charter.website.domain.home.api.entity.HomeWallpaperEntity;

/**
 * 壁纸服务接口
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeWallpaperService {

    /**
     * 随机获取壁纸
     *
     * @param wallpaperType 壁纸类型
     * @return 壁纸实体
     */
    HomeWallpaperEntity getRandom(Integer wallpaperType);

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
