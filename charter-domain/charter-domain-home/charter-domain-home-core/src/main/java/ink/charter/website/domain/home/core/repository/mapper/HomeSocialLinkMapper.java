package ink.charter.website.domain.home.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.home.api.entity.HomeSocialLinkEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 社交链接Mapper
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper
public interface HomeSocialLinkMapper extends BaseMapper<HomeSocialLinkEntity> {

    /**
     * 查询所有启用的社交链接
     *
     * @return 社交链接列表
     */
    default List<HomeSocialLinkEntity> selectAllEnabled() {
        return selectList(QueryWrappers.<HomeSocialLinkEntity>lambdaQuery()
            .eq(HomeSocialLinkEntity::getStatus, 1)
            .orderByAsc(HomeSocialLinkEntity::getSortOrder)
            .orderByDesc(HomeSocialLinkEntity::getCreateTime));
    }
}
