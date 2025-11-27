package ink.charter.website.domain.home.api.converter;

import ink.charter.website.domain.home.api.entity.HomeSiteConfigEntity;
import ink.charter.website.domain.home.api.vo.HomeSiteConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 站点配置转换器
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper(componentModel = "spring")
public interface HomeSiteConfigConverter {

    HomeSiteConfigConverter INSTANCE = Mappers.getMapper(HomeSiteConfigConverter.class);

    /**
     * Entity转换为VO
     */
    HomeSiteConfigVO toVO(HomeSiteConfigEntity entity);

    /**
     * Entity列表转换为VO列表
     */
    List<HomeSiteConfigVO> toVOList(List<HomeSiteConfigEntity> entities);
}
