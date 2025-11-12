package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.common.core.entity.sys.SysOptLogEntity;
import ink.charter.website.domain.admin.api.vo.optlog.OptLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 操作日志转换器
 *
 * @author charter
 * @create 2025/11/12
 */
@Mapper(componentModel = "spring")
public interface OptLogConverter {

    OptLogConverter INSTANCE = Mappers.getMapper(OptLogConverter.class);

    /**
     * SysOptLogEntity转换为OptLogVO
     */
    OptLogVO toVO(SysOptLogEntity entity);

    /**
     * 批量转换为OptLogVO列表
     */
    List<OptLogVO> toVOList(List<SysOptLogEntity> entities);
}
