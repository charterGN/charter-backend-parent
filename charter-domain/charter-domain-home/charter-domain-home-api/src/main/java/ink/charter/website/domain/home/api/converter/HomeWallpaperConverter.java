package ink.charter.website.domain.home.api.converter;

import ink.charter.website.domain.home.api.entity.HomeWallpaperEntity;
import ink.charter.website.domain.home.api.vo.HomeWallpaperVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 壁纸转换器
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper(componentModel = "spring")
public interface HomeWallpaperConverter {

    HomeWallpaperConverter INSTANCE = Mappers.getMapper(HomeWallpaperConverter.class);

    /**
     * Entity转换为VO
     */
    HomeWallpaperVO toVO(HomeWallpaperEntity entity);

    /**
     * Entity列表转换为VO列表
     */
    List<HomeWallpaperVO> toVOList(List<HomeWallpaperEntity> entities);
}
