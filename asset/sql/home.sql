-- ========================================
-- Charter Website 门户数据库建表语句
-- 适用于 MySQL 8.0+ 和 JDK 17
-- 时间字段统一使用 DATETIME 类型
-- ========================================

-- ========================================
-- 门户配置相关表
-- ========================================

-- 1. 站点配置表
CREATE TABLE `home_site_config` (
  `id` BIGINT NOT NULL COMMENT '配置ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键（唯一标识）',
  `config_value` TEXT COMMENT '配置值（JSON格式）',
  `config_type` VARCHAR(50) NOT NULL COMMENT '配置类型（site/wallpaper/weather/hitokoto/other）',
  `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
  `description` VARCHAR(500) COMMENT '配置描述',
  `is_system` TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统配置（0否 1是，系统配置不可删除）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` ((CASE WHEN `is_deleted` = 0 THEN `config_key` END)),
  KEY `idx_config_type` (`config_type`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门户站点配置表';

-- 2. 壁纸管理表
CREATE TABLE `home_wallpaper` (
  `id` BIGINT NOT NULL COMMENT '壁纸ID',
  `wallpaper_name` VARCHAR(100) NOT NULL COMMENT '壁纸名称',
  `wallpaper_type` TINYINT NOT NULL COMMENT '壁纸类型（0默认 1每日一图 2随机风景 3随机动漫）',
  `file_id` BIGINT COMMENT '关联文件ID（sys_files表）',
  `file_url` VARCHAR(500) COMMENT '壁纸URL（外链或本地路径）',
  `thumbnail_url` VARCHAR(500) COMMENT '缩略图URL',
  `from_source` VARCHAR(100) COMMENT '壁纸来源（bing/unsplash/pixiv等）',
  `author` VARCHAR(100) COMMENT '作者',
  `description` VARCHAR(500) COMMENT '壁纸描述',
  `tags` VARCHAR(200) COMMENT '标签（逗号分隔）',
  `width` INT COMMENT '图片宽度',
  `height` INT COMMENT '图片高度',
  `file_size` BIGINT COMMENT '文件大小（字节）',
  `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认壁纸（0否 1是）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_wallpaper_type` (`wallpaper_type`),
  KEY `idx_file_id` (`file_id`),
  KEY `idx_is_default` (`is_default`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门户壁纸管理表';

-- 3. 网站链接表
CREATE TABLE `home_site_link` (
  `id` BIGINT NOT NULL COMMENT '链接ID',
  `link_name` VARCHAR(100) NOT NULL COMMENT '链接名称',
  `link_url` VARCHAR(500) NOT NULL COMMENT '链接地址',
  `icon` VARCHAR(100) COMMENT '图标标识（对应前端图标组件名）',
  `icon_url` VARCHAR(500) COMMENT '图标URL（自定义图标）',
  `description` VARCHAR(500) COMMENT '链接描述',
  `is_external` TINYINT NOT NULL DEFAULT 1 COMMENT '是否外链（0否 1是）',
  `open_type` TINYINT NOT NULL DEFAULT 1 COMMENT '打开方式（0当前窗口 1新窗口）',
  `click_count` INT DEFAULT 0 COMMENT '点击次数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用 2开发中）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门户网站链接表';

-- 4. 社交链接表
CREATE TABLE `home_social_link` (
  `id` BIGINT NOT NULL COMMENT '社交链接ID',
  `platform_name` VARCHAR(50) NOT NULL COMMENT '平台名称（Github/QQ/微信等）',
  `platform_code` VARCHAR(50) NOT NULL COMMENT '平台编码（github/qq/wechat等）',
  `link_url` VARCHAR(500) NOT NULL COMMENT '链接地址',
  `icon` VARCHAR(100) COMMENT '图标标识（对应前端图标组件名）',
  `icon_url` VARCHAR(500) COMMENT '图标URL（自定义图标）',
  `hover_tip` VARCHAR(100) COMMENT '鼠标悬停提示文本',
  `qr_code_url` VARCHAR(500) COMMENT '二维码URL（如微信/QQ）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0禁用 1启用）',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_platform_code` ((CASE WHEN `is_deleted` = 0 THEN `platform_code` END)),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门户社交链接表';

-- 5. 一言库表
CREATE TABLE `home_hitokoto` (
  `id` BIGINT NOT NULL COMMENT '一言ID',
  `hitokoto` VARCHAR(500) NOT NULL COMMENT '一言内容',
  `type` VARCHAR(10) NOT NULL COMMENT '类型（a动画/b漫画/c游戏/d文学/e原创/f网络/g其他/h影视/i诗词/j网易云/k哲学/l抖机灵）',
  `from_source` VARCHAR(100) NOT NULL COMMENT '来源',
  `from_who` VARCHAR(100) COMMENT '作者',
  `creator` VARCHAR(50) COMMENT '添加者',
  `length` INT NOT NULL COMMENT '句子长度',
  `uuid` VARCHAR(36) COMMENT 'UUID（对接Hitokoto API）',
  `view_count` INT DEFAULT 0 COMMENT '展示次数',
  `like_count` INT DEFAULT 0 COMMENT '点赞次数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_length` (`length`),
  KEY `idx_status` (`status`),
  KEY `idx_view_count` (`view_count`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门户一言库表';

-- 6. 访问日志表
CREATE TABLE `home_visit_log` (
  `id` BIGINT NOT NULL COMMENT '日志ID',
  `visit_ip` VARCHAR(50) NOT NULL COMMENT '访问IP',
  `visit_address` VARCHAR(200) COMMENT '访问地址（IP解析）',
  `visit_country` VARCHAR(50) COMMENT '国家',
  `visit_province` VARCHAR(50) COMMENT '省份',
  `visit_city` VARCHAR(50) COMMENT '城市',
  `user_agent` VARCHAR(500) COMMENT '用户代理',
  `browser` VARCHAR(50) COMMENT '浏览器',
  `browser_version` VARCHAR(50) COMMENT '浏览器版本',
  `os` VARCHAR(50) COMMENT '操作系统',
  `device_type` VARCHAR(20) COMMENT '设备类型（PC/Mobile/Tablet）',
  `referer` VARCHAR(500) COMMENT '来源页面',
  `visit_page` VARCHAR(200) COMMENT '访问页面',
  `visit_time` DATETIME NOT NULL COMMENT '访问时间',
  `stay_duration` INT COMMENT '停留时长（秒）',
  `session_id` VARCHAR(128) COMMENT '会话ID',
  `is_new_visitor` TINYINT DEFAULT 1 COMMENT '是否新访客（0否 1是）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_visit_ip` (`visit_ip`),
  KEY `idx_visit_time` (`visit_time`),
  KEY `idx_visit_city` (`visit_city`),
  KEY `idx_device_type` (`device_type`),
  KEY `idx_session_id` (`session_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门户访问日志表';

-- ========================================
-- 建表完成提示
-- ========================================