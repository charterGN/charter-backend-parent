package ink.charter.website.domain.home.api.converter;

import ink.charter.website.domain.home.api.entity.HomeSocialLinkEntity;
import ink.charter.website.domain.home.api.vo.HomeSocialLinkVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 社交链接转换器
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper(componentModel = "spring")
public interface HomeSocialLinkConverter {

    HomeSocialLinkConverter INSTANCE = Mappers.getMapper(HomeSocialLinkConverter.class);

    /**
     * Entity转换为VO
     */
    HomeSocialLinkVO toVO(HomeSocialLinkEntity entity);

    /**
     * Entity列表转换为VO列表
     */
    List<HomeSocialLinkVO> toVOList(List<HomeSocialLinkEntity> entities);
}
