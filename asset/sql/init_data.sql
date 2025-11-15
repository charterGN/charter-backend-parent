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

-- 插入个人中心菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(18, 1, '个人中心', 'personagePage', 2, '/system/personage', 'system/personage/index', 'User', 7, 1, 0, 0, 0, NOW(), NOW(), 0);

-- 插入资源管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(19, 1, '资源管理', 'resourcePage', 2, '/system/resource', 'system/resource/index', 'Platform', 8, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入文件配置菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(20, 1, '文件配置', 'fileConfigPage', 2, '/system/fileconfig', 'system/fileconfig/index', 'FolderChecked', 9, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 插入文件管理菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_code, menu_type, path, component, icon, sort_order, status, visible, cache, external_link, create_time, update_time, is_deleted) VALUES
(21, 1, '文件管理', 'filesPage', 2, '/system/files', 'system/files/index', 'FolderOpened', 10, 1, 1, 0, 0, NOW(), NOW(), 0);

-- 为管理员角色分配菜单权限
INSERT INTO sys_role_menu (id, role_id, menu_id, create_time, update_time, is_deleted) VALUES
(1, 1, 1, NOW(), NOW(), 0),
(2, 1, 11, NOW(), NOW(), 0),
(3, 1, 12, NOW(), NOW(), 0),
(4, 1, 13, NOW(), NOW(), 0),
(5, 1, 14, NOW(), NOW(), 0),
(6, 1, 15, NOW(), NOW(), 0),
(7, 1, 16, NOW(), NOW(), 0),
(8, 1, 17, NOW(), NOW(), 0),
(9, 1, 18, NOW(), NOW(), 0),
(10, 1, 19, NOW(), NOW(), 0),
(11, 1, 20, NOW(), NOW(), 0),
(12, 1, 21, NOW(), NOW(), 0);

-- 插入必要字典类型及数据
-- 菜单类型及数据
INSERT INTO `charter_website`.`sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1988127433409560577, '菜单类型', 'menu_type', 1, '系统菜单类型', '2025-11-11 14:11:32', '2025-11-11 14:11:32', 0);
INSERT INTO `charter_website`.`sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `dict_tag`, `dict_color`, `status`, `sort_order`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1988127568277405697, 'menu_type', '目录', '1', 'warning', '', 1, 1, '', '2025-11-11 14:12:05', '2025-11-12 13:49:25', 0);
INSERT INTO `charter_website`.`sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `dict_tag`, `dict_color`, `status`, `sort_order`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1988127616734199809, 'menu_type', '菜单', '2', 'success', '', 1, 2, '', '2025-11-11 14:12:16', '2025-11-11 14:12:16', 0);
INSERT INTO `charter_website`.`sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `dict_tag`, `dict_color`, `status`, `sort_order`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1988127656873689089, 'menu_type', '按钮', '3', 'info', '', 1, 3, '', '2025-11-11 14:12:26', '2025-11-12 13:35:10', 0);
-- 文件存储类型及数据
INSERT INTO `charter_website`.`sys_dict_type` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1989249564419796993, '文件存储类型', 'file_storage_type', 1, '系统文件存储类型', '2025-11-14 16:30:29', '2025-11-14 16:30:29', 0);
INSERT INTO `charter_website`.`sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `dict_tag`, `dict_color`, `status`, `sort_order`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1989251750650425345, 'file_storage_type', '阿里云OSS', 'oss', 'success', '', 1, 0, '', '2025-11-14 16:39:11', '2025-11-14 16:52:56', 0);
INSERT INTO `charter_website`.`sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `dict_tag`, `dict_color`, `status`, `sort_order`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1989251815439839233, 'file_storage_type', 'MinIO', 'minio', 'primary', '', 1, 1, '', '2025-11-14 16:39:26', '2025-11-14 16:52:08', 0);
INSERT INTO `charter_website`.`sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `dict_tag`, `dict_color`, `status`, `sort_order`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1989252037083639810, 'file_storage_type', '华为云OBS', 'huawei', 'info', '', 1, 2, '', '2025-11-14 16:40:19', '2025-11-14 16:52:12', 0);
INSERT INTO `charter_website`.`sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `dict_tag`, `dict_color`, `status`, `sort_order`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1989252357398441985, 'file_storage_type', '腾讯云COS', 'tencent', 'warning', '', 1, 3, '', '2025-11-14 16:41:35', '2025-11-14 16:52:17', 0);
INSERT INTO `charter_website`.`sys_dict_data` (`id`, `dict_type`, `dict_label`, `dict_value`, `dict_tag`, `dict_color`, `status`, `sort_order`, `remark`, `create_time`, `update_time`, `is_deleted`) VALUES
(1989252493096759298, 'file_storage_type', '七牛云', 'qiniu', 'danger', '', 1, 4, '', '2025-11-14 16:42:08', '2025-11-14 16:52:21', 0);

-- 插入必要的资源
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983034595414016, '删除用户', 'user:delete', '用户管理', '/api-admin/admin/user/delete', 'POST', '删除指定用户', 1, '2025-11-12 16:27:57', '2025-11-12 16:27:57', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983034597511168, '更新用户信息', 'user:update', '用户管理', '/api-admin/admin/user/update', 'POST', '更新指定用户的信息', 1, '2025-11-12 16:27:57', '2025-11-12 16:27:57', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983034597904384, '分页查询用户列表', 'user:query', '用户管理', '/api-admin/admin/user/page', 'POST', '分页查询用户列表', 1, '2025-11-12 16:27:57', '2025-11-12 16:27:57', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983034599870464, '创建用户', 'user:create', '用户管理', '/api-admin/admin/user/create', 'POST', '创建新用户', 1, '2025-11-12 16:27:57', '2025-11-12 16:27:57', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983041249153024, '批量删除用户', 'user:batch-delete', '用户管理', '/api-admin/admin/user/batchDelete', 'POST', '批量删除指定用户', 1, '2025-11-12 16:28:48', '2025-11-12 16:28:48', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531097198592, '批量删除菜单', 'sys:menu:batch-delete', '菜单管理', '/api-admin/sysMenu/batchDelete', 'POST', '批量删除菜单', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531098509312, '使用户其他会话失效', 'sys:session:invalidateOthers', '用户会话管理', '/api-admin/sysSession/user/invalidateOthers', 'POST', '使指定用户的其他会话失效（除了当前会话）', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531100082176, '角色分配菜单权限', 'sys:menu:save-role-menu', '菜单管理', '/api-admin/sysMenu/saveRoleMenu/{roleId}/{menuIds}', 'POST', '保存角色与菜单的关联关系', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531100475392, '查询资源列表', 'sys:resource:page', '资源管理', '/api-admin/sysResource/page', 'POST', '条件查询资源列表', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531101130752, '删除字典类型', 'sys:dictType:delete', '字典类型管理', '/api-admin/sysDictType/deleteById/{id}', 'POST', '删除指定字典类型', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531101917184, '批量删除操作日志', 'sys:optLog:batch-delete', '操作日志管理', '/api-admin/sysOptLog/batch/deleted', 'POST', '根据ID列表批量删除操作日志', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531102703616, '清理过期会话', 'sys:session:cleanup', '用户会话管理', '/api-admin/sysSession/cleanup', 'POST', '清理所有过期的会话', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531103227904, '清空操作日志', 'sys:optLog:truncate', '操作日志管理', '/api-admin/sysOptLog/truncate', 'POST', '清空所有操作日志', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531103621120, '修改资源状态', 'sys:resource:change-status', '资源管理', '/api-admin/sysResource/change-status', 'POST', '启用或禁用指定资源', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531103883264, '删除角色', 'sys:role:delete', '角色管理', '/api-admin/sysRole/delete', 'POST', '删除指定角色', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531104276480, '删除菜单', 'sys:menu:delete', '菜单管理', '/api-admin/sysMenu/delete', 'POST', '根据菜单ID删除菜单', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531105587200, '修改用户密码', 'user:change-password', '用户管理', '/api-admin/admin/user/change-password', 'POST', '修改指定用户的密码', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531106111488, '角色分配资源权限', 'sys:resource:saveRoleResources', '资源管理', '/api-admin/sysResource/role/saveResources', 'POST', '保存角色资源关联关系', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531106504704, '删除操作日志', 'sys:optLog:delete', '操作日志管理', '/api-admin/sysOptLog/deleted/{id}', 'POST', '根据ID删除操作日志', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531107160064, '修改用户状态', 'user:change-status', '用户管理', '/api-admin/admin/user/change-status', 'POST', '启用或禁用指定用户', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531107422208, '用户分配角色权限', 'sys:role:user:save-roles', '角色管理', '/api-admin/sysRole/user/saveRoles', 'POST', '保存用户角色关联关系', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531107815424, '更新字典类型状态', 'sys:dictType:update-status', '字典类型管理', '/api-admin/sysDictType/updateStatus/{id}/{status}', 'POST', '启用或禁用指定字典类型', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531108208640, '更新字典类型', 'sys:dictType:update', '字典类型管理', '/api-admin/sysDictType/update', 'POST', '更新指定字典类型的信息', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531108470784, '使用户所有会话失效', 'sys:session:invalidateAll', '用户会话管理', '/api-admin/sysSession/user/invalidateAll', 'POST', '使指定用户的所有会话失效', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531108995072, '创建角色', 'sys:role:create', '角色管理', '/api-admin/sysRole/create', 'POST', '创建新的角色', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531109388288, '创建菜单', 'sys:menu:create', '菜单管理', '/api-admin/sysMenu/create', 'POST', '创建新菜单', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531109650432, '创建字典类型', 'sys:dictType:add', '字典类型管理', '/api-admin/sysDictType/add', 'POST', '创建新的字典类型', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531110043648, '更新菜单状态', 'sys:menu:update-status', '菜单管理', '/api-admin/sysMenu/updateStatus/{id}/{status}', 'POST', '更新菜单状态', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531110567936, '更新角色信息', 'sys:role:update', '角色管理', '/api-admin/sysRole/update', 'POST', '更新指定角色的信息', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531110830080, '分页查询用户会话', 'sys:session:page', '用户会话管理', '/api-admin/sysSession/page', 'POST', '分页查询用户会话列表', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531111223296, '分页查询操作日志', 'sys:optLog:page', '操作日志管理', '/api-admin/sysOptLog/page', 'POST', '分页查询操作日志列表', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531111747584, '修改角色状态', 'sys:role:update-status', '角色管理', '/api-admin/sysRole/change-status', 'POST', '启用或禁用指定角色', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531112140800, '更新菜单', 'sys:menu:update', '菜单管理', '/api-admin/sysMenu/update', 'POST', '更新菜单信息', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531112665088, '更新资源信息', 'sys:resource:updateInfo', '资源管理', '/api-admin/sysResource/updateInfo', 'POST', '更新资源名称和描述', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531113189376, '分页查询角色列表', 'sys:role:page', '角色管理', '/api-admin/sysRole/page', 'POST', '分页查询角色列表', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531113582592, '查询菜单', 'sys:menu:page', '菜单管理', '/api-admin/sysMenu/page', 'POST', '条件查询菜单列表', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531113975808, '批量删除字典类型', 'sys:dictType:batch-delete', '字典类型管理', '/api-admin/sysDictType/batchDelete', 'POST', '批量删除字典类型', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531114500096, '批量删除角色', 'sys:role:batch-delete', '角色管理', '/api-admin/sysRole/batchDelete', 'POST', '批量删除指定角色', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531115024384, '使会话失效', 'sys:session:invalidate', '用户会话管理', '/api-admin/sysSession/token/invalidate', 'POST', '使指定的会话失效', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);
INSERT INTO `charter_website`.`sys_resource` (`id`, `resource_name`, `resource_code`, `module`, `url`, `method`, `description`, `status`, `create_time`, `update_time`, `is_deleted`) VALUES (15983531115417600, '分页查询字典类型', 'sys:dictType:list', '字典类型管理', '/api-admin/sysDictType/listPage', 'POST', '分页查询字典类型列表', 1, '2025-11-12 17:31:05', '2025-11-12 17:31:05', 0);

-- 给管理员角色分配资源权限
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (1, 1, 15983034595414016, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (2, 1, 15983034597511168, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (3, 1, 15983034597904384, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (4, 1, 15983034599870464, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (5, 1, 15983041249153024, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (6, 1, 15983531097198592, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (7, 1, 15983531098509312, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (8, 1, 15983531100082176, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (9, 1, 15983531100475392, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (10, 1, 15983531101130752, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (11, 1, 15983531101917184, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (12, 1, 15983531102703616, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (13, 1, 15983531103227904, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (14, 1, 15983531103621120, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (15, 1, 15983531103883264, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (16, 1, 15983531104276480, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (17, 1, 15983531105587200, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (18, 1, 15983531106111488, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (19, 1, 15983531106504704, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (20, 1, 15983531107160064, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (21, 1, 15983531107422208, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (22, 1, 15983531107815424, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (23, 1, 15983531108208640, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (24, 1, 15983531108470784, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (25, 1, 15983531108995072, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (26, 1, 15983531109388288, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (27, 1, 15983531109650432, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (28, 1, 15983531110043648, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (29, 1, 15983531110567936, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (30, 1, 15983531110830080, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (31, 1, 15983531111223296, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (32, 1, 15983531111747584, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (33, 1, 15983531112140800, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (34, 1, 15983531112665088, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (35, 1, 15983531113189376, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (36, 1, 15983531113582592, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (37, 1, 15983531113975808, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (38, 1, 15983531114500096, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (39, 1, 15983531115024384, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);
INSERT INTO `charter_website`.`sys_role_resource` (`id`, `role_id`, `resource_id`, `create_time`, `update_time`, `is_deleted`) VALUES (40, 1, 15983531115417600, '2025-11-13 08:50:40', '2025-11-13 08:50:40', 0);