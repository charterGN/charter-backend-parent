package ink.charter.website.domain.home.api.repository;

import ink.charter.website.domain.home.api.entity.HomeSiteLinkEntity;

import java.util.List;

/**
 * 网站链接领域仓库
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeSiteLinkRepository {

    /**
     * 获取所有启用的网站链接
     *
     * @return 链接列表
     */
    List<HomeSiteLinkEntity> getAllEnabled();

    /**
     * 增加点击次数
     *
     * @param id 链接ID
     * @return 是否成功
     */
    boolean incrementClickCount(Long id);
}
