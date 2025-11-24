package ink.charter.website.domain.home.api.repository;

import ink.charter.website.domain.home.api.entity.HomeSocialLinkEntity;

import java.util.List;

/**
 * 社交链接领域仓库
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeSocialLinkRepository {

    /**
     * 获取所有启用的社交链接
     *
     * @return 链接列表
     */
    List<HomeSocialLinkEntity> getAllEnabled();
}
