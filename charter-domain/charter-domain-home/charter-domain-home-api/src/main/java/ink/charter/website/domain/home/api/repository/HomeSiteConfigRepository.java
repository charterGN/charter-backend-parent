package ink.charter.website.domain.home.api.repository;

import ink.charter.website.domain.home.api.entity.HomeSiteConfigEntity;

import java.util.List;

/**
 * 站点配置领域仓库
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeSiteConfigRepository {

    /**
     * 获取所有启用的站点配置
     *
     * @return 配置列表
     */
    List<HomeSiteConfigEntity> getAllEnabled();

    /**
     * 根据配置类型获取配置列表
     *
     * @param configType 配置类型
     * @return 配置列表
     */
    List<HomeSiteConfigEntity> getByConfigType(String configType);
}
