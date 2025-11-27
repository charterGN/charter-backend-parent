package ink.charter.website.domain.home.api.converter;

import ink.charter.website.domain.home.api.entity.HomeSiteLinkEntity;
import ink.charter.website.domain.home.api.vo.HomeSiteLinkVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 网站链接转换器
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper(componentModel = "spring")
public interface HomeSiteLinkConverter {

    HomeSiteLinkConverter INSTANCE = Mappers.getMapper(HomeSiteLinkConverter.class);

    /**
     * Entity转换为VO
     */
    HomeSiteLinkVO toVO(HomeSiteLinkEntity entity);

    /**
     * Entity列表转换为VO列表
     */
    List<HomeSiteLinkVO> toVOList(List<HomeSiteLinkEntity> entities);
}
