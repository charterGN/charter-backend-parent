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
  UNIQUE KEY `uk_username` ((CASE WHEN `is_deleted` = 0 THEN `username` END)),
  UNIQUE KEY `uk_email` ((CASE WHEN `is_deleted` = 0 THEN `email` END)),
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
  UNIQUE KEY `uk_role_code` ((CASE WHEN `is_deleted` = 0 THEN `role_code` END)),
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
  `cache` TINYINT DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
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
  `module` VARCHAR(50) NOT NULL COMMENT '所属模块',
  `url` VARCHAR(500) COMMENT '资源URL',
  `method` VARCHAR(10) COMMENT 'HTTP方法',
  `description` VARCHAR(200) COMMENT '资源描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resource_code` ((CASE WHEN `is_deleted` = 0 THEN `resource_code` END)),
  KEY `idx_module` (`module`),
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
  UNIQUE KEY `uk_user_role` ((CASE WHEN `is_deleted` = 0 THEN CONCAT(`user_id`, '-', `role_id`) END)),
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
  UNIQUE KEY `uk_role_menu` ((CASE WHEN `is_deleted` = 0 THEN CONCAT(`role_id`, '-', `menu_id`) END)),
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
  UNIQUE KEY `uk_role_resource` ((CASE WHEN `is_deleted` = 0 THEN CONCAT(`role_id`, '-', `resource_id`) END)),
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
  UNIQUE KEY `uk_session_id` ((CASE WHEN `is_deleted` = 0 THEN `session_id` END)),
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

-- ========================================
-- 文件表
-- ========================================

-- 文件信息表
CREATE TABLE `sys_files` (
  `id` BIGINT NOT NULL COMMENT '文件ID',
  `file_name` VARCHAR(255) NOT NULL COMMENT '文件名称',
  `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
  `file_md5` VARCHAR(32) NOT NULL COMMENT '文件MD5值',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件存储路径',
  `file_type` VARCHAR(255) COMMENT '文件类型',
  `upload_user_id` BIGINT COMMENT '上传用户ID',
  `upload_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `last_access_time` DATETIME COMMENT '最后一次访问时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '文件状态（0不可用 1可用）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_file_md5` ((CASE WHEN `is_deleted` = 0 THEN `file_md5` END)),
  KEY `idx_upload_user_id` (`upload_user_id`),
  KEY `idx_file_type` (`file_type`),
  KEY `idx_status` (`status`),
  KEY `idx_upload_time` (`upload_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统文件信息表';

-- ========================================
-- 定时任务相关表
-- ========================================

-- 定时任务配置表
CREATE TABLE `sys_job` (
  `id` BIGINT NOT NULL COMMENT '任务ID',
  `job_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
  `job_group` VARCHAR(50) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务分组',
  `job_class` VARCHAR(255) NOT NULL COMMENT '任务执行类',
  `cron_expression` VARCHAR(100) NOT NULL COMMENT 'Cron表达式',
  `description` VARCHAR(500) COMMENT '任务描述',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0暂停 1启用)',
  `concurrent` TINYINT NOT NULL DEFAULT 0 COMMENT '是否并发(0禁止 1允许)',
  `misfire_policy` TINYINT DEFAULT 1 COMMENT '错过执行策略(1立即执行 2执行一次 3放弃执行)',
  `retry_count` INT DEFAULT 0 COMMENT '重试次数',
  `timeout` INT DEFAULT 0 COMMENT '超时时间(秒)',
  `params` TEXT COMMENT '任务参数(JSON格式)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_job_name_group` ((CASE WHEN `is_deleted` = 0 THEN CONCAT(`job_name`, '-', `job_group`) END)),
  KEY `idx_status` (`status`),
  KEY `idx_job_group` (`job_group`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务配置表';

-- 任务执行日志表
CREATE TABLE `sys_job_log` (
  `id` BIGINT NOT NULL COMMENT '日志ID',
  `job_id` BIGINT NOT NULL COMMENT '任务ID',
  `job_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
  `job_group` VARCHAR(50) NOT NULL COMMENT '任务分组',
  `execute_time` DATETIME NOT NULL COMMENT '执行时间',
  `finish_time` DATETIME COMMENT '完成时间',
  `execute_status` TINYINT NOT NULL COMMENT '执行状态(0失败 1成功 2执行中)',
  `cost_time` BIGINT COMMENT '耗时(毫秒)',
  `error_msg` TEXT COMMENT '错误信息',
  `server_ip` VARCHAR(50) COMMENT '执行服务器IP',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_execute_time` (`execute_time`),
  KEY `idx_execute_status` (`execute_status`),
  KEY `idx_job_name_group` (`job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务执行日志表';

-- ========================================
-- 字典管理相关表
-- ========================================

-- 字典类型表
CREATE TABLE `sys_dict_type` (
  `id` BIGINT NOT NULL COMMENT '字典类型ID',
  `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称',
  `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type` ((CASE WHEN `is_deleted` = 0 THEN `dict_type` END)),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';

-- 字典数据表
CREATE TABLE `sys_dict_data` (
  `id` BIGINT NOT NULL COMMENT '字典数据ID',
  `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
  `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
  `dict_value` VARCHAR(100) NOT NULL COMMENT '字典键值',
  `dict_tag` VARCHAR(50) COMMENT '标签类型',
  `dict_color` VARCHAR(50) COMMENT '标签颜色',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_dict_type` (`dict_type`),
  KEY `idx_dict_value` (`dict_value`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 建表完成提示
-- ========================================