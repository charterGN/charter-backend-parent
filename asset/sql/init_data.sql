-- 基本初始化数据
-- 创建基本用户（用户名：admin  密码：123456）
INSERT INTO `charter_website`.`sys_user` (`id`, `username`, `nickname`, `email`, `phone`, `avatar`, `password`, `salt`, `status`, `login_count`, `last_login_time`, `last_login_ip`, `create_time`, `update_time`, `is_deleted`) VALUES
(1949096558420389890, 'admin', '管理员', '2088694379@qq.com', '15179634423', 'https://charter-project-parent.oss-cn-shenzhen.aliyuncs.com/aurora/avatar/59671bbc5beed2c5193b54c1fa4b952f.png', '$2a$10$iGZBVFyGCjKrKLx/oIw.muCUg1n1b.nkxYQjnWTu5QqfeJXWrF1re', 'EP6Z08WtONL5w1p087+mkg==', 1, 9, '2025-11-08 12:00:03', '192.168.1.6', '2025-07-26 21:16:47', '2025-11-08 12:00:03', 0);

-- 创建管理员角色
INSERT INTO sys_role (id, role_name, role_code, description, status, create_time, update_time, is_deleted) VALUES
(1, '超级管理员', 'ADMIN', '系统超级管理员角色', 1, NOW(), NOW(), 0);

-- 为用户分配管理员角色
INSERT INTO sys_user_role (id, user_id, role_id, create_time, update_time, is_deleted) VALUES
(1, 1949096558420389890, 1, NOW(), NOW(), 0);

-- 插入系统管理目录
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(1, 0, '系统管理', 'systemPage', 1, '/system', '', 'Tools', 1, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入用户管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(11, 1, '用户管理', 'userPage', 2, '/system/user', 'system/user/index', 'UserFilled', 1, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入角色管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(12, 1, '角色管理', 'rolePage', 2, '/system/role', 'system/role/index', 'Avatar', 2, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入菜单管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(13, 1, '菜单管理', 'menuPage', 2, '/system/menu', 'system/menu/index', 'Menu', 3, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入字典管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(14, 1, '字典管理', 'dictTypePage', 2, '/system/dict/type', 'system/dict/type', 'WalletFilled', 4, 1, 1, 0, 0, NOW(), NOW(), 0);
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(15, 14, '字典数据', 'dictDataPage', 2, '/system/dict/data', 'system/dict/data', 'WalletFilled', 1, 1, 0, 0, 0, NOW(), NOW(), 0);

-- 插入会话管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(16, 1, '会话管理', 'sessionPage', 2, '/system/session', 'system/session/index', 'ChatLineSquare', 5, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入操作日志菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(17, 1, '操作日志', 'optLogPage', 2, '/system/operlog', 'system/operlog/index', 'InfoFilled', 6, 1, 1, 0, 0, NOW(), NOW(), 0);


-- 为管理员角色分配菜单权限
INSERT INTO sys_role_menu (id, role_id, menu_id, create_time, update_time, is_deleted) VALUES
(1, 1, 1, NOW(), NOW(), 0),
(2, 1, 11, NOW(), NOW(), 0),
(3, 1, 12, NOW(), NOW(), 0),
(4, 1, 13, NOW(), NOW(), 0),
(5, 1, 14, NOW(), NOW(), 0),
(6, 1, 15, NOW(), NOW(), 0),
(7, 1, 16, NOW(), NOW(), 0),
(8, 1, 17, NOW(), NOW(), 0);