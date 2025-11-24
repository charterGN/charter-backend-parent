package ink.charter.website.domain.home.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.home.api.entity.HomeSiteLinkEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 网站链接Mapper
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper
public interface HomeSiteLinkMapper extends BaseMapper<HomeSiteLinkEntity> {

    /**
     * 查询所有启用的网站链接
     *
     * @return 网站链接列表
     */
    default List<HomeSiteLinkEntity> selectAllEnabled() {
        return selectList(QueryWrappers.<HomeSiteLinkEntity>lambdaQuery()
            .eq(HomeSiteLinkEntity::getStatus, 1)
            .orderByAsc(HomeSiteLinkEntity::getSortOrder)
            .orderByDesc(HomeSiteLinkEntity::getCreateTime));
    }

    /**
     * 增加点击次数
     *
     * @param id 链接ID
     * @return 更新行数
     */
    default int incrementClickCount(Long id) {
        if (id == null) {
            return 0;
        }
        return update(null, QueryWrappers.<HomeSiteLinkEntity>lambdaUpdate()
            .setSql("click_count = click_count + 1")
            .eq(HomeSiteLinkEntity::getId, id));
    }
}
