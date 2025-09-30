package ink.charter.website.server.admin.sys.service.impl;

import ink.charter.website.common.auth.utils.SecurityUtils;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysUserEntity;
import ink.charter.website.common.core.enums.StatusEnum;
import ink.charter.website.domain.admin.api.repository.SysUserRepository;
import ink.charter.website.server.admin.sys.converter.UserConverter;
import ink.charter.website.domain.admin.api.dto.user.PageUserDTO;
import ink.charter.website.server.admin.sys.service.UserService;
import ink.charter.website.domain.admin.api.vo.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 用户服务实现类
 *
 * @author charter
 * @create 2025/07/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserRepository sysUserRepository;
    private final UserConverter userConverter;

    @Override
    public PageResult<UserVO> pageUsers(PageUserDTO pageRequest) {
        PageResult<SysUserEntity> pageResult = sysUserRepository.pageUsers(pageRequest);
        if (pageResult.hasData()) {
            List<SysUserEntity> records = pageResult.getRecords();
            return PageResult.of(userConverter.toVOList(records), pageResult.getTotal());
        }
        return PageResult.empty();
    }

    @Override
    public SysUserEntity getUserByUsername(String username) {
        return sysUserRepository.getUserByUsername(username);
    }

    @Override
    public SysUserEntity getUserByEmail(String email) {
        return sysUserRepository.getUserByEmail(email);
    }

    @Override
    public SysUserEntity getUserByUsernameOrEmail(String usernameOrEmail) {
        return sysUserRepository.getUserByUsernameOrEmail(usernameOrEmail);
    }

    @Override
    public SysUserEntity getUserById(Long userId) {
        return sysUserRepository.getUserById(userId);
    }

    @Override
    public Set<String> getUserPermissions(Long userId) {
        return sysUserRepository.getUserPermissions(userId);
    }

    @Override
    public Set<String> getUserRoles(Long userId) {
        return sysUserRepository.getUserRoles(userId);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return sysUserRepository.getRoleIdsByUserId(userId);
    }

    @Override
    public boolean validatePassword(SysUserEntity user, String rawPassword) {
        if (user == null || !StringUtils.hasText(rawPassword)) {
            return false;
        }
        
        // 使用BCrypt验证密码
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    @Override
    public void updateLoginInfo(Long userId, String loginIp) {
        sysUserRepository.updateLoginInfo(userId, loginIp);
    }

    @Override
    public boolean isUserStatusNormal(SysUserEntity user) {
        if (user == null) {
            return false;
        }
        // 检查用户状态：1-启用，0-禁用
        return StatusEnum.ENABLED.getCode().equals(user.getStatus());
    }

    @Override
    public boolean createUser(SysUserEntity user) {
        return sysUserRepository.createUser(user);
    }

    @Override
    public boolean updateUser(SysUserEntity user) {
        return sysUserRepository.updateUser(user);
    }

    @Override
    public boolean deleteUser(Long userId) {
        return sysUserRepository.deleteUser(userId);
    }
}