-- ========================================
-- Charter Website 门户初始化数据
-- 适用于 MySQL 8.0+ 和 JDK 17
-- ========================================

-- ========================================
-- 一、站点配置初始化数据
-- ========================================

-- 站点基本配置
INSERT INTO `home_site_config` (`id`, `config_key`, `config_value`, `config_type`, `config_name`, `description`, `is_system`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(1, 'site_name', '羡鱼の主页', 'site', '站点名称', '网站的名称', 1, 1, 1, NOW(), NOW(), 0),
(2, 'site_description', '一个默默无闻的主页', 'site', '站点描述', '网站的描述信息', 1, 1, 2, NOW(), NOW(), 0),
(3, 'site_keywords', '羡鱼,charter,个人主页', 'site', '站点关键词', '网站的SEO关键词', 1, 1, 3, NOW(), NOW(), 0),
(4, 'site_author', 'charter', 'site', '站点作者', '网站作者', 1, 1, 4, NOW(), NOW(), 0),
(5, 'site_url', 'charter.ink', 'site', '站点地址', '网站的域名地址', 1, 1, 5, NOW(), NOW(), 0),
(6, 'site_icp', '赣ICP备2023002431号', 'site', 'ICP备案号', '网站ICP备案号', 1, 1, 6, NOW(), NOW(), 0),
(7, 'site_start_date', '2023-10-29', 'site', '建站日期', '网站建站日期', 1, 1, 7, NOW(), NOW(), 0);

-- 壁纸配置
INSERT INTO `home_site_config` (`id`, `config_key`, `config_value`, `config_type`, `config_name`, `description`, `is_system`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(11, 'wallpaper_default_type', '0', 'wallpaper', '默认壁纸类型', '默认显示的壁纸类型（0默认/1每日一图/2随机风景/3随机动漫）', 1, 1, 1, NOW(), NOW(), 0);

-- 一言配置
INSERT INTO `home_site_config` (`id`, `config_key`, `config_value`, `config_type`, `config_name`, `description`, `is_system`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(21, 'hitokoto_types', 'a,d,i', 'hitokoto', '一言类型', '启用的一言类型（a动画/d文学/i诗词）', 1, 1, 1, NOW(), NOW(), 0),
(22, 'hitokoto_auto_refresh', 'true', 'hitokoto', '自动刷新', '是否自动刷新一言', 1, 1, 2, NOW(), NOW(), 0),
(23, 'hitokoto_refresh_interval', '30', 'hitokoto', '刷新间隔', '一言自动刷新间隔（分钟）', 1, 1, 3, NOW(), NOW(), 0);

-- ========================================
-- 二、壁纸初始化数据
-- ========================================

-- 默认壁纸（本地壁纸）
INSERT INTO `home_wallpaper` (`id`, `wallpaper_name`, `wallpaper_type`, `file_url`, `thumbnail_url`, `from_source`, `author`, `description`, `width`, `height`, `is_default`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(1, '默认壁纸1', 0, '/images/background1.jpg', '/images/background1.jpg', 'local', 'charter', '默认本地壁纸', 1920, 1080, 1, 1, 1, NOW(), NOW(), 0),
(2, '默认壁纸2', 0, '/images/background2.jpg', '/images/background2.jpg', 'local', 'charter', '默认本地壁纸', 1920, 1080, 0, 1, 2, NOW(), NOW(), 0),
(3, '默认壁纸3', 0, '/images/background3.jpg', '/images/background3.jpg', 'local', 'charter', '默认本地壁纸', 1920, 1080, 0, 1, 3, NOW(), NOW(), 0),
(4, '默认壁纸4', 0, '/images/background4.jpg', '/images/background4.jpg', 'local', 'charter', '默认本地壁纸', 1920, 1080, 0, 1, 4, NOW(), NOW(), 0),
(5, '默认壁纸5', 0, '/images/background5.jpg', '/images/background5.jpg', 'local', 'charter', '默认本地壁纸', 1920, 1080, 0, 1, 5, NOW(), NOW(), 0);

-- 每日一图壁纸（Bing）
INSERT INTO `home_wallpaper` (`id`, `wallpaper_name`, `wallpaper_type`, `file_url`, `from_source`, `description`, `is_default`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(11, 'Bing每日一图', 1, 'https://api.dujin.org/bing/1920.php', 'bing', 'Bing每日精选壁纸', 0, 1, 1, NOW(), NOW(), 0);

-- 随机风景壁纸
INSERT INTO `home_wallpaper` (`id`, `wallpaper_name`, `wallpaper_type`, `file_url`, `from_source`, `description`, `is_default`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(21, '随机风景壁纸', 2, 'https://api.aixiaowai.cn/gqapi/gqapi.php', 'aixiaowai', '随机风景壁纸', 0, 1, 1, NOW(), NOW(), 0);

-- 随机动漫壁纸
INSERT INTO `home_wallpaper` (`id`, `wallpaper_name`, `wallpaper_type`, `file_url`, `from_source`, `description`, `is_default`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(31, '随机动漫壁纸', 3, 'https://api.aixiaowai.cn/api/api.php', 'aixiaowai', '随机动漫壁纸', 0, 1, 1, NOW(), NOW(), 0);

-- ========================================
-- 三、网站链接初始化数据
-- ========================================

INSERT INTO `home_site_link` (`id`, `link_name`, `link_url`, `icon`, `description`, `is_external`, `open_type`, `click_count`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(1, '博客', 'https://blog.charter.ink/', 'Blog', 'Charter的个人博客', 1, 1, 0, 1, 1, NOW(), NOW(), 0),
(2, '起始页', 'https://nav.charter.ink/', 'Compass', '个人导航页', 1, 1, 0, 1, 2, NOW(), NOW(), 0),
(3, '音乐', '', 'CompactDisc', '音乐播放器', 1, 1, 0, 2, 3, NOW(), NOW(), 0),
(4, '网盘', 'https://disk.charter.ink/', 'Cloud', '个人网盘', 1, 1, 0, 1, 4, NOW(), NOW(), 0),
(5, '网址集', '', 'Book', '网址收藏', 1, 1, 0, 2, 5, NOW(), NOW(), 0),
(6, '今日热榜', '', 'Fire', '今日热门资讯', 1, 1, 0, 2, 6, NOW(), NOW(), 0),
(7, '站点监测', 'https://status.charter.ink/', 'LaptopCode', '网站状态监测', 1, 1, 0, 1, 7, NOW(), NOW(), 0),
(8, 'AI助手', 'http://120.79.21.15:8088/chat/', 'Robot', 'AI聊天助手', 1, 1, 0, 1, 8, NOW(), NOW(), 0);

-- ========================================
-- 四、社交链接初始化数据
-- ========================================

INSERT INTO `home_social_link` (`id`, `platform_name`, `platform_code`, `link_url`, `icon_url`, `hover_tip`, `status`, `sort_order`, `create_time`, `update_time`, `is_deleted`) VALUES
(1, 'Github', 'github', 'https://github.com/charterGN', '/images/icon/github.png', '去 Github 看看', 1, 1, NOW(), NOW(), 0),
(2, 'BiliBili', 'bilibili', 'https://space.bilibili.com/287780175', '/images/icon/bilibili.png', '(゜-゜)つロ 干杯 ~', 1, 2, NOW(), NOW(), 0),
(3, 'QQ', 'qq', 'https://res.abeim.cn/api/qq/?qq=2088694379', '/images/icon/qq.png', '有什么事吗', 1, 3, NOW(), NOW(), 0),
(4, 'Email', 'email', 'https://wx.mail.qq.com/', '/images/icon/email.png', '来封 Email ~', 1, 4, NOW(), NOW(), 0),
(5, 'Twitter', 'twitter', 'https://twitter.com', '/images/icon/twitter.png', '你懂的 ~', 1, 5, NOW(), NOW(), 0),
(6, 'Telegram', 'telegram', 'https://telegram.org/', '/images/icon/telegram.png', '你懂的 ~', 1, 6, NOW(), NOW(), 0);

-- ========================================
-- 五、一言初始化数据
-- ========================================

-- 动画类一言
INSERT INTO `home_hitokoto` (`id`, `hitokoto`, `type`, `from_source`, `from_who`, `creator`, `length`, `view_count`, `like_count`, `create_time`, `update_time`, `is_deleted`) VALUES
(1, '人生如逆旅，我亦是行人。', 'a', '动漫', '未知', 'system', 12, 0, 0, NOW(), NOW(), 0),
(2, '我们的征途是星辰大海。', 'a', '银河英雄传说', '莱因哈特', 'system', 12, 0, 0, NOW(), NOW(), 0),
(3, '只要有想见的人，就不再是孤身一人了。', 'a', '夏目友人帐', '夏目贵志', 'system', 19, 0, 0, NOW(), NOW(), 0),
(4, '人生就像一盒巧克力，你永远不知道下一颗是什么味道。', 'a', '阿甘正传', '阿甘', 'system', 26, 0, 0, NOW(), NOW(), 0),
(5, '不要温和地走进那个良夜。', 'a', '星际穿越', '迪伦·托马斯', 'system', 13, 0, 0, NOW(), NOW(), 0);

-- 文学类一言
INSERT INTO `home_hitokoto` (`id`, `hitokoto`, `type`, `from_source`, `from_who`, `creator`, `length`, `view_count`, `like_count`, `create_time`, `update_time`, `is_deleted`) VALUES
(11, '生活不止眼前的苟且，还有诗和远方的田野。', 'd', '文学', '高晓松', 'system', 22, 0, 0, NOW(), NOW(), 0),
(12, '世界上只有一种真正的英雄主义，那就是认清生活的真相后依然热爱生活。', 'd', '文学', '罗曼·罗兰', 'system', 35, 0, 0, NOW(), NOW(), 0),
(13, '你的问题主要在于读书不多而想得太多。', 'd', '文学', '杨绛', 'system', 19, 0, 0, NOW(), NOW(), 0),
(14, '我们听过无数的道理，却仍旧过不好这一生。', 'd', '后会无期', '韩寒', 'system', 22, 0, 0, NOW(), NOW(), 0),
(15, '人生天地之间，若白驹过隙，忽然而已。', 'd', '庄子', '庄子', 'system', 19, 0, 0, NOW(), NOW(), 0);

-- 诗词类一言
INSERT INTO `home_hitokoto` (`id`, `hitokoto`, `type`, `from_source`, `from_who`, `creator`, `length`, `view_count`, `like_count`, `create_time`, `update_time`, `is_deleted`) VALUES
(21, '人生若只如初见，何事秋风悲画扇。', 'i', '木兰花令·拟古决绝词', '纳兰性德', 'system', 17, 0, 0, NOW(), NOW(), 0),
(22, '山有木兮木有枝，心悦君兮君不知。', 'i', '越人歌', '佚名', 'system', 17, 0, 0, NOW(), NOW(), 0),
(23, '愿得一心人，白首不相离。', 'i', '白头吟', '卓文君', 'system', 12, 0, 0, NOW(), NOW(), 0),
(24, '在天愿作比翼鸟，在地愿为连理枝。', 'i', '长恨歌', '白居易', 'system', 17, 0, 0, NOW(), NOW(), 0),
(25, '曾经沧海难为水，除却巫山不是云。', 'i', '离思五首·其四', '元稹', 'system', 17, 0, 0, NOW(), NOW(), 0),
(26, '人生自古谁无死，留取丹心照汗青。', 'i', '过零丁洋', '文天祥', 'system', 17, 0, 0, NOW(), NOW(), 0),
(27, '海内存知己，天涯若比邻。', 'i', '送杜少府之任蜀州', '王勃', 'system', 13, 0, 0, NOW(), NOW(), 0),
(28, '但愿人长久，千里共婵娟。', 'i', '水调歌头', '苏轼', 'system', 13, 0, 0, NOW(), NOW(), 0),
(29, '春风得意马蹄疾，一日看尽长安花。', 'i', '登科后', '孟郊', 'system', 17, 0, 0, NOW(), NOW(), 0),
(30, '莫愁前路无知己，天下谁人不识君。', 'i', '别董大', '高适', 'system', 17, 0, 0, NOW(), NOW(), 0);

-- ========================================
-- 初始化完成提示
-- ========================================
-- 门户初始化数据已完成
-- 包含：站点配置、壁纸、网站链接、社交链接、一言等基础数据
