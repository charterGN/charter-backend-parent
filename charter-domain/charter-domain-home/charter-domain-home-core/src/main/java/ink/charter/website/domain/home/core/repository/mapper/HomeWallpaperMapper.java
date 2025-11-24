package ink.charter.website.domain.home.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.home.api.entity.HomeWallpaperEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 壁纸Mapper
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper
public interface HomeWallpaperMapper extends BaseMapper<HomeWallpaperEntity> {

    /**
     * 查询所有启用的壁纸
     *
     * @param wallpaperType 壁纸类型
     * @return 壁纸列表
     */
    default List<HomeWallpaperEntity> getAllEnable(Integer wallpaperType){
        return selectList(QueryWrappers.<HomeWallpaperEntity>lambdaQuery()
            .eq(HomeWallpaperEntity::getStatus, 1)
            .eq(HomeWallpaperEntity::getWallpaperType, wallpaperType));
    }

    /**
     * 查询默认壁纸
     *
     * @return 默认壁纸
     */
    default HomeWallpaperEntity selectDefault() {
        return selectOne(QueryWrappers.<HomeWallpaperEntity>lambdaQuery()
            .eq(HomeWallpaperEntity::getIsDefault, 1)
            .eq(HomeWallpaperEntity::getStatus, 1)
            .last("LIMIT 1"));
    }

    /**
     * 增加浏览次数
     *
     * @param id 壁纸ID
     * @return 更新行数
     */
    default int incrementViewCount(Long id) {
        if (id == null) {
            return 0;
        }
        return update(null, QueryWrappers.<HomeWallpaperEntity>lambdaUpdate()
            .setSql("view_count = view_count + 1")
            .eq(HomeWallpaperEntity::getId, id));
    }

    /**
     * 增加下载次数
     *
     * @param id 壁纸ID
     * @return 更新行数
     */
    default int incrementDownloadCount(Long id) {
        if (id == null) {
            return 0;
        }
        return update(null, QueryWrappers.<HomeWallpaperEntity>lambdaUpdate()
            .setSql("download_count = download_count + 1")
            .eq(HomeWallpaperEntity::getId, id));
    }
}
