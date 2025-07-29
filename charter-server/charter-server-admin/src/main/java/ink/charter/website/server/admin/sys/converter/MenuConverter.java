package ink.charter.website.server.admin.sys.converter;

import ink.charter.website.common.core.entity.sys.SysMenuEntity;
import ink.charter.website.server.admin.sys.vo.menu.DynamicMenuVO;
import ink.charter.website.server.admin.sys.vo.menu.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 菜单转换器
 *
 * @author charter
 * @create 2025/07/28
 */
@Mapper(componentModel = "spring")
public interface MenuConverter {

    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);

    /**
     * 将实体类转换为VO对象
     *
     * @param entity 实体类
     * @return VO对象
     */
    @Mapping(source = "id", target = "menuId")
    @Mapping(source = "menuCode", target = "name")
    @Mapping(source = "menuType", target = "menuType", expression = "java(String.valueOf(entity.getMenuType()))")
    @Mapping(source = "visible", target = "isHide", expression = "java(entity.getVisible() == 1 ? \"1\" : \"0\")")
    @Mapping(source = "externalLink", target = "isLink", expression = "java(entity.getExternalLink() == 1 ? \"1\" : \"0\")")
    @Mapping(source = "cache", target = "isKeepAlive", expression = "java(entity.getCache() == 1 ? \"1\" : \"0\")")
    @Mapping(target = "isTag", constant = "1")
    @Mapping(target = "isAffix", constant = "0")
    @Mapping(target = "redirect", constant = "")
    DynamicMenuVO convertToVO(SysMenuEntity entity);

    /**
     * 将实体类列表转换为VO对象列表
     *
     * @param entityList 实体类列表
     * @return VO对象列表
     */
    List<DynamicMenuVO> convertToVOList(List<SysMenuEntity> entityList);

    /**
     * 将实体类转换为MenuVO对象
     *
     * @param entity 实体类
     * @return MenuVO对象
     */
    @Mapping(source = "id", target = "menuId")
    MenuVO convertToMenuVO(SysMenuEntity entity);

    /**
     * 将实体类列表转换为MenuVO对象列表
     *
     * @param entityList 实体类列表
     * @return MenuVO对象列表
     */
    List<MenuVO> convertToMenuVOList(List<SysMenuEntity> entityList);
}