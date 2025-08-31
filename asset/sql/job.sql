-- ========================================
-- Charter Website 定时任务相关表
-- 适用于 MySQL 8.0+ 和 JDK 17
-- ========================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  UNIQUE KEY `uk_job_name_group` (`job_name`, `job_group`),
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

-- 插入测试数据
INSERT INTO `sys_job` (`id`, `job_name`, `job_group`, `job_class`, `cron_expression`, `description`, `status`, `concurrent`, `misfire_policy`, `retry_count`, `timeout`, `params`) VALUES
(1, 'demoJob', 'DEFAULT', 'demoJob', '0 */5 * * * ?', '演示任务，每5分钟执行一次', 1, 0, 1, 3, 300, '{"message":"Hello World"}'),
(2, 'cleanLogJob', 'SYSTEM', 'cleanLogJob', '0 0 2 * * ?', '清理日志任务，每天凌晨2点执行', 1, 0, 2, 1, 600, '{"days":30}');

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 建表完成提示
-- ========================================
-- 定时任务相关表创建完成
-- 包含：sys_job（任务配置表）、sys_job_log（执行日志表）