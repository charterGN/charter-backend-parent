package ink.charter.website.server.home.converter;

import ink.charter.website.domain.home.api.entity.HomeVisitLogEntity;
import ink.charter.website.domain.home.api.vo.HomeVisitLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 访问日志转换器
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper(componentModel = "spring")
public interface HomeVisitLogConverter {

    HomeVisitLogConverter INSTANCE = Mappers.getMapper(HomeVisitLogConverter.class);

    /**
     * Entity转换为VO
     */
    HomeVisitLogVO toVO(HomeVisitLogEntity entity);

    /**
     * Entity列表转换为VO列表
     */
    List<HomeVisitLogVO> toVOList(List<HomeVisitLogEntity> entities);
}
