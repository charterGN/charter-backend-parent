package ink.charter.website.server.admin.converter;

import ink.charter.website.server.admin.vo.auth.LoginResponseVO;
import ink.charter.website.server.admin.vo.auth.UserInfoVO;
import ink.charter.website.common.auth.model.LoginResponse;
import ink.charter.website.common.auth.model.LoginUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 认证相关转换器
 *
 * @author charter
 * @create 2025/07/17
 */
@Mapper(componentModel = "spring")
public interface AuthConverter {

    AuthConverter INSTANCE = Mappers.getMapper(AuthConverter.class);

    /**
     * LoginUser转换为UserInfoVO
     *
     * @param loginUser 登录用户信息
     * @return 用户信息VO
     */
    UserInfoVO toUserInfoVO(LoginUser loginUser);

    /**
     * LoginResponse转换为LoginResponseVO
     *
     * @param loginResponse 登录响应
     * @return 登录响应VO
     */
    @Mapping(source = "userInfo", target = "userInfo")
    LoginResponseVO toLoginResponseVO(LoginResponse loginResponse);
}