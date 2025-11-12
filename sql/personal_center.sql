-- 用户标签表
CREATE TABLE IF NOT EXISTS `sys_user_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `type` varchar(20) NOT NULL DEFAULT 'info' COMMENT '标签类型（info, success, warning, error）',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户标签表';

-- 待办事项表
CREATE TABLE IF NOT EXISTS `sys_todo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '待办事项ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(200) NOT NULL COMMENT '待办事项标题',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0未完成 1已完成）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='待办事项表';

-- 系统消息表
CREATE TABLE IF NOT EXISTS `sys_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `title` varchar(100) NOT NULL COMMENT '消息标题',
  `content` varchar(500) NOT NULL COMMENT '消息内容',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态（0未读 1已读）',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '消息类型（0系统消息 1通知公告 2待办提醒）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统消息表';

ALTER TABLE `sys_message` ADD INDEX `idx_user_id_status` (`user_id`, `status`);

-- 添加用户表的bio字段
ALTER TABLE `sys_user` ADD COLUMN `bio` varchar(500) DEFAULT NULL COMMENT '个人简介' AFTER `avatar`;

-- 插入一些示例数据
-- 用户标签示例数据
INSERT INTO `sys_user_tag` (`user_id`, `name`, `type`) VALUES
(1, '技术控', 'info'),
(1, '爱学习', 'success'),
(1, '大嘴巴', 'warning'),
(1, '宅男', 'error'),
(1, '嘚嘚没完', 'info'),
(1, 'UP主', 'success'),
(1, '手机控', 'warning');

-- 待办事项示例数据
INSERT INTO `sys_todo` (`user_id`, `title`, `status`) VALUES
(1, '和朋友同事一起玩王者，吃鸡', 0),
(1, '下班写今日总结', 1),
(1, '中午打卡，吃饭，下去买一瓶快乐水', 0),
(1, '给项目经理演示项目成果，汇报项目进度', 1),
(1, '上班打卡', 0);

-- 系统消息示例数据
INSERT INTO `sys_message` (`user_id`, `title`, `content`, `status`, `type`) VALUES
(1, '【总经理通知】', '明天【下午】有【不拘一格】课程直播，公司尝试全新直播模式...', 0, 1),
(1, '重要通知：今天要加班', '为了配合市场家人们努力开单，从今天开始，技术部及教研老师们要努力加班...', 0, 1),
(1, '系统更新通知', '系统将于今晚进行例行维护更新，请及时保存工作内容...', 1, 0); 
