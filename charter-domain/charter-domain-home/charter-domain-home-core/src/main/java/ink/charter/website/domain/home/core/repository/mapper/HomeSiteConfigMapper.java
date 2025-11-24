package ink.charter.website.domain.home.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import ink.charter.website.domain.home.api.entity.HomeSiteConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 站点配置Mapper
 *
 * @author charter
 * @create 2025/11/24
 */
@Mapper
public interface HomeSiteConfigMapper extends BaseMapper<HomeSiteConfigEntity> {

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @return 配置列表
     */
    default List<HomeSiteConfigEntity> selectByConfigType(String configType) {
        if (!StringUtils.hasText(configType)) {
            return List.of();
        }
        return selectList(QueryWrappers.<HomeSiteConfigEntity>lambdaQuery()
            .eq(HomeSiteConfigEntity::getConfigType, configType)
            .eq(HomeSiteConfigEntity::getStatus, 1)
            .orderByAsc(HomeSiteConfigEntity::getSortOrder));
    }

    /**
     * 查询所有启用的配置
     *
     * @return 配置列表
     */
    default List<HomeSiteConfigEntity> selectAllEnabled() {
        return selectList(QueryWrappers.<HomeSiteConfigEntity>lambdaQuery()
            .eq(HomeSiteConfigEntity::getStatus, 1)
            .orderByAsc(HomeSiteConfigEntity::getSortOrder));
    }
}
