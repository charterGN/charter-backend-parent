package ink.charter.website.server.home.converter;

import ink.charter.website.domain.home.api.entity.HomeHitokotoEntity;
import ink.charter.website.domain.home.api.vo.HomeHitokotoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 一言转换器
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper(componentModel = "spring")
public interface HomeHitokotoConverter {

    HomeHitokotoConverter INSTANCE = Mappers.getMapper(HomeHitokotoConverter.class);

    /**
     * Entity转换为VO
     */
    HomeHitokotoVO toVO(HomeHitokotoEntity entity);

    /**
     * Entity列表转换为VO列表
     */
    List<HomeHitokotoVO> toVOList(List<HomeHitokotoEntity> entities);
}
