-- ========================================
-- Charter Website 数据库建表语句
-- 适用于 MySQL 8.0+ 和 JDK 17
-- 时间字段统一使用 DATETIME 类型
-- ========================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- 用户管理相关表
-- ========================================

-- 用户表
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `email` VARCHAR(100) COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '手机号',
  `avatar` VARCHAR(500) COMMENT '头像URL',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  `salt` VARCHAR(32) COMMENT '密码盐值',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `login_count` INT DEFAULT 0 COMMENT '登录次数',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 角色表
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL COMMENT '角色ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `description` VARCHAR(200) COMMENT '角色描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- 菜单表
CREATE TABLE `sys_menu` (
  `id` BIGINT NOT NULL COMMENT '菜单ID',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID（0为顶级菜单）',
  `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `menu_code` VARCHAR(50) COMMENT '菜单编码',
  `menu_type` TINYINT NOT NULL COMMENT '菜单类型（1目录 2菜单 3按钮）',
  `path` VARCHAR(200) COMMENT '路由路径',
  `component` VARCHAR(200) COMMENT '组件路径',
  `icon` VARCHAR(100) COMMENT '菜单图标',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示（0隐藏 1显示）',
  `cache` TINYINT DEFAULT 0 COMMENT '是否缓存（0不缓存 1缓存）',
  `external_link` TINYINT DEFAULT 0 COMMENT '是否外链（0否 1是）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_menu_type` (`menu_type`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单表';

-- 资源表
CREATE TABLE `sys_resource` (
  `id` BIGINT NOT NULL COMMENT '资源ID',
  `resource_name` VARCHAR(100) NOT NULL COMMENT '资源名称',
  `resource_code` VARCHAR(100) NOT NULL COMMENT '资源编码',
  `resource_type` TINYINT NOT NULL COMMENT '资源类型（1接口 2文件 3数据）',
  `url` VARCHAR(500) COMMENT '资源URL',
  `method` VARCHAR(10) COMMENT 'HTTP方法',
  `description` VARCHAR(200) COMMENT '资源描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resource_code` (`resource_code`),
  KEY `idx_resource_type` (`resource_type`),
  KEY `idx_status` (`status`),
  KEY `idx_url_method` (`url`, `method`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统资源表';

-- ========================================
-- 关联关系表
-- ========================================

-- 用户角色关联表
CREATE TABLE `sys_user_role` (
  `id` BIGINT NOT NULL COMMENT '关联ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色菜单关联表
CREATE TABLE `sys_role_menu` (
  `id` BIGINT NOT NULL COMMENT '关联ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- 角色资源关联表
CREATE TABLE `sys_role_resource` (
  `id` BIGINT NOT NULL COMMENT '关联ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `resource_id` BIGINT NOT NULL COMMENT '资源ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_resource` (`role_id`, `resource_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_resource_id` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色资源关联表';

-- ========================================
-- 会话和日志表
-- ========================================

-- 用户会话表
CREATE TABLE `sys_user_session` (
  `id` BIGINT NOT NULL COMMENT '会话ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `session_id` VARCHAR(128) NOT NULL COMMENT '会话标识',
  `token` VARCHAR(500) COMMENT '访问令牌',
  `refresh_token` VARCHAR(500) COMMENT '刷新令牌',
  `login_ip` VARCHAR(50) COMMENT '登录IP',
  `login_address` VARCHAR(200) COMMENT '登录地址',
  `user_agent` VARCHAR(500) COMMENT '用户代理',
  `login_time` DATETIME NOT NULL COMMENT '登录时间',
  `expire_time` DATETIME NOT NULL COMMENT '过期时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0离线 1在线）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_id` (`session_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_token` (`token`(100)),
  KEY `idx_status` (`status`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话表';

-- 系统操作日志表
CREATE TABLE `sys_opt_log` (
  `id` BIGINT NOT NULL COMMENT '主键ID',
  `opt_website` VARCHAR(100) COMMENT '操作网站',
  `opt_module` VARCHAR(50) COMMENT '操作模块',
  `opt_type` VARCHAR(20) COMMENT '操作类型',
  `opt_url` VARCHAR(500) COMMENT '操作URL',
  `opt_method` VARCHAR(100) COMMENT '操作方法',
  `opt_desc` VARCHAR(200) COMMENT '操作描述',
  `request_method` VARCHAR(10) COMMENT '请求方式',
  `request_param` TEXT COMMENT '请求参数',
  `response_data` TEXT COMMENT '返回数据',
  `opt_id` BIGINT COMMENT '操作人ID',
  `opt_name` VARCHAR(50) COMMENT '操作人名称',
  `opt_ip` VARCHAR(50) COMMENT '操作IP',
  `opt_address` VARCHAR(200) COMMENT '操作地址',
  `opt_status` TINYINT DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` TEXT COMMENT '异常信息',
  `opt_cost_time` BIGINT COMMENT '操作耗时（毫秒）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_opt_id` (`opt_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_opt_module_type` (`opt_module`, `opt_type`),
  KEY `idx_opt_status` (`opt_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统操作日志表';

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 建表完成提示
-- ========================================