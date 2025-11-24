package ink.charter.website.server.home.service;

import ink.charter.website.domain.home.api.entity.HomeSiteLinkEntity;

import java.util.List;

/**
 * 网站链接服务接口
 *
 * @author charter
 * @create 2025/11/24
 */
public interface HomeSiteLinkService {

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
