-- 菜单测试数据
-- 创建管理员角色
INSERT INTO sys_role (id, role_name, role_code, description, status, create_time, update_time, is_deleted) VALUES
(1, '超级管理员', 'ADMIN', '系统超级管理员角色', 1, NOW(), NOW(), 0);

-- 为用户分配管理员角色
INSERT INTO sys_user_role (id, user_id, role_id, create_time, update_time, is_deleted) VALUES
(1, 1949096558420389890, 1, NOW(), NOW(), 0);

-- 插入系统管理目录
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(1, 0, 'menu.system.auth', 'systemPage', 1, '/system', '', 'Tools', 1, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入用户管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(11, 1, 'menu.system.user.name', 'userPage', 2, '/system/user', 'system/user/index', 'UserFilled', 1, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入角色管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(12, 1, 'menu.system.role.name', 'rolePage', 2, '/system/role', 'system/role/index', 'Avatar', 2, 1, 1, 0, 0, NOW(), NOW(), 0);



-- 为管理员角色分配菜单权限
INSERT INTO sys_role_menu (id, role_id, menu_id, create_time, update_time, is_deleted) VALUES
(1, 1, 1, NOW(), NOW(), 0),
(2, 1, 11, NOW(), NOW(), 0),
(3, 1, 12, NOW(), NOW(), 0);