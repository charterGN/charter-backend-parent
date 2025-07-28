package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.server.admin.sys.vo.menu.DynamicMenuVO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单转换器
 *
 * @author charter
 * @create 2025/07/28
 */
@Component
public class MenuConverter {

    /**
     * 将实体类转换为VO对象
     *
     * @param entity 实体类
     * @return VO对象
     */
    public DynamicMenuVO convertToVO(SysMenuEntity entity) {
        if (entity == null) {
            return null;
        }
        
        DynamicMenuVO vo = new DynamicMenuVO();
        vo.setMenuId(entity.getId());
        vo.setMenuName(entity.getMenuName());
        vo.setParentId(entity.getParentId());
        vo.setMenuType(String.valueOf(entity.getMenuType()));
        vo.setPath(entity.getPath());
        vo.setName(entity.getMenuCode());
        vo.setComponent(entity.getComponent());
        vo.setIcon(entity.getIcon());
        vo.setIsHide(entity.getVisible() == 1 ? "1" : "0");
        vo.setIsLink(entity.getExternalLink() == 1 ? "1" : "0");
        vo.setIsKeepAlive(entity.getCache() == 1 ? "1" : "0");
        // 默认设置为可显示标签
        vo.setIsTag("1");
        // 默认设置为不固定
        vo.setIsAffix("0");
        // 如果是目录，设置重定向到第一个子菜单
        if (entity.getMenuType() == 1) {
            vo.setRedirect("");
        } else {
            vo.setRedirect("");
        }
        
        return vo;
    }

    /**
     * 将实体类列表转换为VO对象列表
     *
     * @param entityList 实体类列表
     * @return VO对象列表
     */
    public List<DynamicMenuVO> convertToVOList(List<SysMenuEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        
        return entityList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
}