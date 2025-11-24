package ink.charter.website.server.home.service;

import ink.charter.website.domain.home.api.entity.HomeSocialLinkEntity;

import java.util.List;

/**
 * 社交链接服务接口
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeSocialLinkService {

    /**
     * 获取所有启用的社交链接
     *
     * @return 链接列表
     */
    List<HomeSocialLinkEntity> getAllEnabled();
}
