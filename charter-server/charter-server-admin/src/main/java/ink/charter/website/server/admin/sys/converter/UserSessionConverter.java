package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.domain.admin.api.vo.session.UserSessionVO;
import ink.charter.website.common.core.entity.sys.SysUserSessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户会话转换器
 *
 * @author charter
 * @create 2025/07/17
 */
@Mapper(componentModel = "spring")
public interface UserSessionConverter {

    UserSessionConverter INSTANCE = Mappers.getMapper(UserSessionConverter.class);

    /**
     * SysUserSessionEntity转换为UserSessionVO
     */
    @Mapping(target = "sessionId", source = "id")
    @Mapping(target = "expired", expression = "java(entity.getExpireTime() != null && entity.getExpireTime().isBefore(java.time.LocalDateTime.now()))")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "sessionToken", source = "token")
    UserSessionVO toVO(SysUserSessionEntity entity);

    /**
     * 批量转换为UserSessionVO列表
     */
    List<UserSessionVO> toVOList(List<SysUserSessionEntity> entities);
}