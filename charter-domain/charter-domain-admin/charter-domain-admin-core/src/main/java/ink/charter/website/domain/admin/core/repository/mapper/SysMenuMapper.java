package ink.charter.website.domain.admin.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.common.mybatis.wrapper.QueryWrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统菜单Mapper接口
 *
 * @author charter
 * @create 2025/07/28
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {

    /**
     * 查询所有正常状态的菜单
     *
     * @return 菜单列表
     */
    default List<SysMenuEntity> selectNormalMenus() {
        return selectList(QueryWrappers.<SysMenuEntity>lambdaQuery()
                .eq(SysMenuEntity::getStatus, 1)
                .orderByAsc(SysMenuEntity::getSortOrder));
    }
}