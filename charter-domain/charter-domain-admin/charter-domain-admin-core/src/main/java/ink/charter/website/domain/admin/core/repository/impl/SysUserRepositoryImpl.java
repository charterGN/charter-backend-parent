package ink.charter.website.domain.admin.core.repository.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.charter.website.common.auth.utils.SecurityUtils;
import ink.charter.website.common.core.common.PageResult;
import ink.charter.website.common.core.entity.sys.SysUserEntity;
import ink.charter.website.domain.admin.api.dto.user.PageUserDTO;
import ink.charter.website.domain.admin.api.repository.SysUserRepository;
import ink.charter.website.domain.admin.core.repository.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 系统用户领域仓库实现
 * @author charter
 * @create 2025/07/23
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SysUserRepositoryImpl implements SysUserRepository {

    private final SysUserMapper sysUserMapper;

    @Override
    public PageResult<SysUserEntity> pageUsers(PageUserDTO pageRequest) {
        IPage<SysUserEntity> entityIPage = sysUserMapper.pageUsers(pageRequest);
        return PageResult.of(entityIPage.getRecords(), entityIPage.getTotal());
    }

    @Override
    public SysUserEntity getUserByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public SysUserEntity getUserByEmail(String email) {
        return sysUserMapper.selectByEmail(email);
    }

    @Override
    public SysUserEntity getUserByUsernameOrEmail(String usernameOrEmail) {
        if (!StringUtils.hasText(usernameOrEmail)) {
            return null;
        }
        return sysUserMapper.selectByUsernameOrEmail(usernameOrEmail);
    }

    @Override
    public SysUserEntity getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        return sysUserMapper.selectById(userId);
    }

    @Override
    public Set<String> getUserPermissions(Long userId) {
        if (userId == null) {
            return Set.of();
        }
        return sysUserMapper.selectUserPermissions(userId);
    }

    @Override
    public Set<String> getUserRoles(Long userId) {
        if (userId == null) {
            return Set.of();
        }
        return sysUserMapper.selectUserRoles(userId);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return sysUserMapper.selectUserRoleIds(userId);
    }

    @Override
    public void updateLoginInfo(Long userId, String loginIp) {
        if (userId != null) {
            sysUserMapper.updateLoginInfo(userId, loginIp);
        }
    }

    @Override
    public boolean createUser(SysUserEntity user) {
        if (user == null) {
            return false;
        }
        
        try {
            // 加密密码
            if (StringUtils.hasText(user.getPassword())) {
                String encodedPassword = SecurityUtils.encodePassword(user.getPassword());
                user.setPassword(encodedPassword);
            }
            
            // 生成盐值
            if (!StringUtils.hasText(user.getSalt())) {
                user.setSalt(SecurityUtils.generateSalt());
            }
            
            return sysUserMapper.insert(user) > 0;
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateUser(SysUserEntity user) {
        if (user == null || user.getId() == null) {
            return false;
        }
        
        try {
            // 如果更新密码，需要重新加密
            if (StringUtils.hasText(user.getPassword())) {
                String encodedPassword = SecurityUtils.encodePassword(user.getPassword());
                user.setPassword(encodedPassword);
                // 更新盐值
                user.setSalt(SecurityUtils.generateSalt());
            }
            
            return sysUserMapper.updateById(user) > 0;
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (userId == null) {
            return false;
        }
        
        try {
            // 使用MyBatis-Plus的逻辑删除功能
            return sysUserMapper.deleteById(userId) > 0;
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean batchDeleteUsers(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }

        try {
            // 使用MyBatis-Plus的批量逻辑删除功能
            return sysUserMapper.deleteByIds(userIds) > 0;
        } catch (Exception e) {
            log.error("批量删除用户失败: {}", e.getMessage(), e);
            return false;
        }
    }
}
