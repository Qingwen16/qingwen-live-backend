-- =====================================================
-- 青问直播平台 - 数据库初始化脚本
-- 数据库：MySQL 8.0+
-- 字符集：utf8mb4
-- 创建时间：2026-03-14
-- =====================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `qingwen_live`
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE `qingwen_live`;

-- =====================================================
-- 1. 用户信息表
-- =====================================================
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
    `id` BIGINT NOT NULL COMMENT 'ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `password` VARCHAR(255) DEFAULT NULL COMMENT '用户密码',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '用户手机',
    `status` TINYINT DEFAULT 1 COMMENT '用户状态 0-禁用 1-正常',
    `deleted` TINYINT DEFAULT 1 COMMENT '是否注销 0-注销 1-未注销',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_phone` (`phone`),
    KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- =====================================================
-- 2. 主播信息表
-- =====================================================
DROP TABLE IF EXISTS `anchor_info`;
CREATE TABLE `anchor_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主播 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID（关联 UserInfo）',
    `anchor_name` VARCHAR(50) DEFAULT NULL COMMENT '主播昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '主播头像',
    `description` TEXT COMMENT '主播简介',
    `level` INT DEFAULT 1 COMMENT '主播等级',
    `fans_count` BIGINT DEFAULT 0 COMMENT '粉丝数量',
    `follow_count` BIGINT DEFAULT 0 COMMENT '关注数量',
    `total_view_count` BIGINT DEFAULT 0 COMMENT '总观看次数',
    `total_gift_value` BIGINT DEFAULT 0 COMMENT '总收到礼物价值',
    `status` TINYINT DEFAULT 1 COMMENT '主播状态 0-禁用 1-正常 2-封禁',
    `deleted` TINYINT DEFAULT 1 COMMENT '是否注销 0-注销 1-未注销',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_anchor_name` (`anchor_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='主播信息表';

-- =====================================================
-- 3. 管理员信息表
-- =====================================================
DROP TABLE IF EXISTS `admin_info`;
CREATE TABLE `admin_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID（关联 UserInfo）',
    `admin_account` VARCHAR(50) DEFAULT NULL COMMENT '管理员账号',
    `admin_name` VARCHAR(50) DEFAULT NULL COMMENT '管理员姓名',
    `admin_type` TINYINT DEFAULT 2 COMMENT '管理员类型 1-超级管理员 2-普通管理员 3-房管',
    `permissions` VARCHAR(500) DEFAULT NULL COMMENT '权限标识',
    `room_ids` TEXT COMMENT '负责的房间 ID 列表（JSON 格式，房管用）',
    `status` TINYINT DEFAULT 1 COMMENT '管理员状态 0-禁用 1-正常',
    `last_login_time` BIGINT DEFAULT NULL COMMENT '最后登录时间',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_admin_account` (`admin_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员信息表';

-- =====================================================
-- 4. 直播分区表
-- =====================================================
DROP TABLE IF EXISTS `live_category`;
CREATE TABLE `live_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分区 ID',
    `category_name` VARCHAR(50) DEFAULT NULL COMMENT '分区名称',
    `category_icon` VARCHAR(255) DEFAULT NULL COMMENT '分区图标',
    `description` TEXT COMMENT '分区描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `is_hot` TINYINT DEFAULT 0 COMMENT '是否热门 0-否 1-是',
    `status` TINYINT DEFAULT 1 COMMENT '分区状态 0-隐藏 1-显示',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='直播分区表';

-- =====================================================
-- 5. 直播间信息表
-- =====================================================
DROP TABLE IF EXISTS `live_room`;
CREATE TABLE `live_room` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '直播间 ID',
    `room_number` VARCHAR(20) DEFAULT NULL COMMENT '房间号（唯一标识）',
    `anchor_id` BIGINT DEFAULT NULL COMMENT '主播 ID（关联 AnchorInfo）',
    `title` VARCHAR(100) DEFAULT NULL COMMENT '直播间标题',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '直播间封面图',
    `category_id` BIGINT DEFAULT NULL COMMENT '分区 ID（关联 LiveCategory）',
    `announcement` TEXT COMMENT '直播间公告',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '直播标签（JSON 数组）',
    `current_viewers` INT DEFAULT 0 COMMENT '当前观看人数',
    `total_viewers` BIGINT DEFAULT 0 COMMENT '累计观看人数',
    `like_count` BIGINT DEFAULT 0 COMMENT '点赞数',
    `follow_count` BIGINT DEFAULT 0 COMMENT '关注数',
    `status` TINYINT DEFAULT 0 COMMENT '直播状态 0-未开播 1-直播中 2-回放 3-关闭',
    `is_recommend` TINYINT DEFAULT 0 COMMENT '是否推荐 0-否 1-是',
    `stream_url` VARCHAR(500) DEFAULT NULL COMMENT '推流地址',
    `play_url` VARCHAR(500) DEFAULT NULL COMMENT '拉流地址',
    `start_time` BIGINT DEFAULT NULL COMMENT '开始直播时间',
    `end_time` BIGINT DEFAULT NULL COMMENT '结束直播时间',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_room_number` (`room_number`),
    KEY `idx_anchor_id` (`anchor_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='直播间信息表';

-- =====================================================
-- 6. 直播记录表
-- =====================================================
DROP TABLE IF EXISTS `live_record`;
CREATE TABLE `live_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录 ID',
    `room_id` BIGINT DEFAULT NULL COMMENT '直播间 ID',
    `anchor_id` BIGINT DEFAULT NULL COMMENT '主播 ID',
    `title` VARCHAR(100) DEFAULT NULL COMMENT '直播标题',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '直播封面',
    `start_time` BIGINT DEFAULT NULL COMMENT '开始时间',
    `end_time` BIGINT DEFAULT NULL COMMENT '结束时间',
    `duration` BIGINT DEFAULT 0 COMMENT '直播时长（秒）',
    `peak_viewers` INT DEFAULT 0 COMMENT '最高观看人数',
    `total_viewers` BIGINT DEFAULT 0 COMMENT '累计观看人数',
    `total_gift_value` BIGINT DEFAULT 0 COMMENT '收到礼物价值',
    `message_count` INT DEFAULT 0 COMMENT '弹幕数量',
    `like_count` BIGINT DEFAULT 0 COMMENT '点赞数量',
    `record_video_url` VARCHAR(500) DEFAULT NULL COMMENT '回放视频 URL',
    `record_status` TINYINT DEFAULT 0 COMMENT '回放状态 0-未生成 1-已生成 2-转码中',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_room_id` (`room_id`),
    KEY `idx_anchor_id` (`anchor_id`),
    KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='直播记录表';

-- =====================================================
-- 7. 聊天消息表（弹幕）
-- =====================================================
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息 ID',
    `room_id` BIGINT DEFAULT NULL COMMENT '直播间 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID',
    `sender_nickname` VARCHAR(50) DEFAULT NULL COMMENT '发送者昵称',
    `sender_avatar` VARCHAR(255) DEFAULT NULL COMMENT '发送者头像',
    `content` TEXT COMMENT '消息内容',
    `color_type` TINYINT DEFAULT 1 COMMENT '弹幕颜色 1-白色 2-红色 3-蓝色 4-绿色等',
    `message_type` TINYINT DEFAULT 1 COMMENT '弹幕类型 1-普通弹幕 2-高级弹幕 3-彩色弹幕',
    `user_level` INT DEFAULT 1 COMMENT '用户等级',
    `fan_medal_level` INT DEFAULT 0 COMMENT '粉丝牌等级',
    `fan_medal_name` VARCHAR(50) DEFAULT NULL COMMENT '粉丝牌名称',
    `is_anchor` TINYINT DEFAULT 0 COMMENT '是否主播发送 0-否 1-是',
    `is_admin` TINYINT DEFAULT 0 COMMENT '是否管理员发送 0-否 1-是',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶 0-否 1-是',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '消息状态 0-待审核 1-正常 2-违规',
    `send_time` BIGINT DEFAULT NULL COMMENT '发送时间',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_room_id` (`room_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='聊天消息表（弹幕）';

-- =====================================================
-- 8. 礼物信息表
-- =====================================================
DROP TABLE IF EXISTS `gift_info`;
CREATE TABLE `gift_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '礼物 ID',
    `gift_name` VARCHAR(50) DEFAULT NULL COMMENT '礼物名称',
    `gift_image` VARCHAR(255) DEFAULT NULL COMMENT '礼物图片',
    `gift_animation_url` VARCHAR(500) DEFAULT NULL COMMENT '礼物动态效果 URL',
    `gift_price` BIGINT DEFAULT 0 COMMENT '礼物价值（虚拟币单位）',
    `gift_type` TINYINT DEFAULT 1 COMMENT '礼物类型 1-普通礼物 2-豪华礼物 3-特殊礼物',
    `gift_level` INT DEFAULT 1 COMMENT '礼物等级',
    `duration` INT DEFAULT 0 COMMENT '赠送时长（毫秒，用于持续礼物）',
    `description` TEXT COMMENT '礼物描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `is_hot` TINYINT DEFAULT 0 COMMENT '是否热门 0-否 1-是',
    `status` TINYINT DEFAULT 1 COMMENT '礼物状态 0-下架 1-上架',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='礼物信息表';

-- =====================================================
-- 9. 礼物赠送记录表
-- =====================================================
DROP TABLE IF EXISTS `gift_record`;
CREATE TABLE `gift_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录 ID',
    `room_id` BIGINT DEFAULT NULL COMMENT '直播间 ID',
    `anchor_id` BIGINT DEFAULT NULL COMMENT '主播 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '赠送用户 ID',
    `sender_nickname` VARCHAR(50) DEFAULT NULL COMMENT '赠送者昵称',
    `sender_avatar` VARCHAR(255) DEFAULT NULL COMMENT '赠送者头像',
    `gift_id` BIGINT DEFAULT NULL COMMENT '礼物 ID',
    `gift_name` VARCHAR(50) DEFAULT NULL COMMENT '礼物名称',
    `gift_image` VARCHAR(255) DEFAULT NULL COMMENT '礼物图片',
    `gift_price` BIGINT DEFAULT 0 COMMENT '礼物单价',
    `gift_count` INT DEFAULT 1 COMMENT '赠送数量',
    `total_price` BIGINT DEFAULT 0 COMMENT '总价值',
    `message` TEXT COMMENT '礼物消息内容（自定义消息）',
    `combo_count` INT DEFAULT 0 COMMENT '连击次数',
    `is_broadcast` TINYINT DEFAULT 0 COMMENT '是否全服广播 0-否 1-是',
    `send_time` BIGINT DEFAULT NULL COMMENT '赠送时间',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_room_id` (`room_id`),
    KEY `idx_anchor_id` (`anchor_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_gift_id` (`gift_id`),
    KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='礼物赠送记录表';

-- =====================================================
-- 10. 用户关注关系表
-- =====================================================
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关注 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID',
    `target_id` BIGINT DEFAULT NULL COMMENT '被关注对象 ID（主播 ID）',
    `follow_type` TINYINT DEFAULT 1 COMMENT '关注类型 1-关注主播 2-关注用户',
    `fan_medal_level` INT DEFAULT 0 COMMENT '粉丝牌等级',
    `fan_medal_name` VARCHAR(50) DEFAULT NULL COMMENT '粉丝牌名称',
    `intimacy` BIGINT DEFAULT 0 COMMENT '亲密度',
    `is_special` TINYINT DEFAULT 0 COMMENT '是否特别关注 0-否 1-是',
    `is_blacklist` TINYINT DEFAULT 0 COMMENT '是否黑名单 0-否 1-是',
    `follow_time` BIGINT DEFAULT NULL COMMENT '关注时间',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_target` (`user_id`, `target_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关注关系表';

-- =====================================================
-- 11. 用户钱包表
-- =====================================================
DROP TABLE IF EXISTS `user_wallet`;
CREATE TABLE `user_wallet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '钱包 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID',
    `balance` BIGINT DEFAULT 0 COMMENT '账户余额（虚拟币）',
    `total_recharge` BIGINT DEFAULT 0 COMMENT '充值总额',
    `total_consumption` BIGINT DEFAULT 0 COMMENT '消费总额',
    `total_gift_received` BIGINT DEFAULT 0 COMMENT '收到礼物总额',
    `total_gift_sent` BIGINT DEFAULT 0 COMMENT '送出礼物总额',
    `points` BIGINT DEFAULT 0 COMMENT '积分',
    `experience` BIGINT DEFAULT 0 COMMENT '经验值',
    `user_level` INT DEFAULT 1 COMMENT '用户等级',
    `vip_level` INT DEFAULT 0 COMMENT 'VIP 等级 0-普通 1-VIP1 2-VIP2...',
    `vip_expire_time` BIGINT DEFAULT NULL COMMENT 'VIP 到期时间',
    `last_login_time` BIGINT DEFAULT NULL COMMENT '上次登录时间',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_balance` (`balance`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户钱包表';

-- =====================================================
-- 12. 充值记录表
-- =====================================================
DROP TABLE IF EXISTS `recharge_record`;
CREATE TABLE `recharge_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID',
    `order_no` VARCHAR(50) DEFAULT NULL COMMENT '订单号',
    `amount` BIGINT DEFAULT 0 COMMENT '充值金额（分）',
    `coins` BIGINT DEFAULT 0 COMMENT '获得虚拟币数量',
    `channel` TINYINT DEFAULT 1 COMMENT '充值渠道 1-微信 2-支付宝 3-银行卡等',
    `recharge_type` TINYINT DEFAULT 1 COMMENT '充值类型 1-手动充值 2-自动充值',
    `pay_status` TINYINT DEFAULT 0 COMMENT '支付状态 0-待支付 1-支付成功 2-支付失败 3-已取消',
    `third_party_order_no` VARCHAR(100) DEFAULT NULL COMMENT '第三方支付订单号',
    `pay_time` BIGINT DEFAULT NULL COMMENT '支付时间',
    `remark` TEXT COMMENT '备注',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pay_status` (`pay_status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值记录表';

-- =====================================================
-- 13. 房间禁言表
-- =====================================================
DROP TABLE IF EXISTS `room_mute`;
CREATE TABLE `room_mute` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录 ID',
    `room_id` BIGINT DEFAULT NULL COMMENT '直播间 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作管理员 ID',
    `reason` TEXT COMMENT '禁言原因',
    `start_time` BIGINT DEFAULT NULL COMMENT '禁言开始时间',
    `end_time` BIGINT DEFAULT NULL COMMENT '禁言结束时间',
    `duration` BIGINT DEFAULT 0 COMMENT '禁言时长（秒）',
    `status` TINYINT DEFAULT 1 COMMENT '禁言状态 0-已解除 1-禁言中',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_room_id` (`room_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间禁言表';

-- =====================================================
-- 14. 系统消息表
-- =====================================================
DROP TABLE IF EXISTS `system_message`;
CREATE TABLE `system_message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息 ID',
    `user_id` BIGINT DEFAULT 0 COMMENT '接收用户 ID（0 表示全员）',
    `message_type` TINYINT DEFAULT 1 COMMENT '消息类型 1-系统通知 2-活动消息 3-警告消息 4-私信',
    `title` VARCHAR(100) DEFAULT NULL COMMENT '消息标题',
    `content` TEXT COMMENT '消息内容',
    `link_url` VARCHAR(500) DEFAULT NULL COMMENT '消息链接',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读 0-未读 1-已读',
    `read_time` BIGINT DEFAULT NULL COMMENT '阅读时间',
    `sender_id` BIGINT DEFAULT 0 COMMENT '发送者 ID（0 表示系统）',
    `send_time` BIGINT DEFAULT NULL COMMENT '发送时间',
    `expire_time` BIGINT DEFAULT NULL COMMENT '过期时间',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统消息表';

-- =====================================================
-- 15. WebSocket 连接记录表
-- =====================================================
DROP TABLE IF EXISTS `websocket_session`;
CREATE TABLE `websocket_session` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID',
    `session_id` VARCHAR(100) DEFAULT NULL COMMENT 'Session ID',
    `room_id` BIGINT DEFAULT NULL COMMENT '连接的房间 ID',
    `client_ip` VARCHAR(50) DEFAULT NULL COMMENT '客户端 IP',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `connect_time` BIGINT DEFAULT NULL COMMENT '连接时间',
    `last_active_time` BIGINT DEFAULT NULL COMMENT '最后活跃时间',
    `disconnect_time` BIGINT DEFAULT NULL COMMENT '断开时间',
    `status` TINYINT DEFAULT 1 COMMENT '连接状态 0-已断开 1-连接中',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_room_id` (`room_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='WebSocket 连接记录表';

-- =====================================================
-- 16. 用户等级配置表
-- =====================================================
DROP TABLE IF EXISTS `user_level_config`;
CREATE TABLE `user_level_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置 ID',
    `level` INT DEFAULT 1 COMMENT '等级',
    `level_name` VARCHAR(50) DEFAULT NULL COMMENT '等级名称',
    `required_experience` BIGINT DEFAULT 0 COMMENT '所需经验值',
    `level_icon` VARCHAR(255) DEFAULT NULL COMMENT '等级图标',
    `level_color` VARCHAR(20) DEFAULT NULL COMMENT '等级颜色',
    `daily_message_limit` INT DEFAULT 100 COMMENT '每日弹幕数量上限',
    `daily_gift_limit` BIGINT DEFAULT 10000 COMMENT '每日赠送礼物价值上限',
    `color_privilege` TINYINT DEFAULT 0 COMMENT '弹幕颜色特权',
    `enter_effect` TINYINT DEFAULT 0 COMMENT '进场特效特权',
    `status` TINYINT DEFAULT 1 COMMENT '等级状态 0-禁用 1-启用',
    `create_time` BIGINT DEFAULT NULL COMMENT '创建时间',
    `update_time` BIGINT DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_level` (`level`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户等级配置表';

-- =====================================================
-- 初始化数据
-- =====================================================

-- 插入默认管理员
INSERT INTO `admin_info` (`user_id`, `admin_account`, `admin_name`, `admin_type`, `permissions`, `status`, `create_time`, `update_time`)
VALUES (1, 'admin', '超级管理员', 1, 'ALL', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000);

-- 插入默认直播分区
INSERT INTO `live_category` (`category_name`, `category_icon`, `description`, `sort_order`, `is_hot`, `status`, `create_time`, `update_time`) VALUES
('颜值', '/icons/beauty.png', '高颜值主播聚集地', 1, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('游戏', '/icons/game.png', '游戏直播专区', 2, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('娱乐', '/icons/entertainment.png', '娱乐表演直播', 3, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('音乐', '/icons/music.png', '音乐才艺展示', 4, 0, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('生活', '/icons/life.png', '生活分享直播', 5, 0, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000);

-- 插入默认礼物
INSERT INTO `gift_info` (`gift_name`, `gift_image`, `gift_animation_url`, `gift_price`, `gift_type`, `gift_level`, `duration`, `description`, `sort_order`, `is_hot`, `status`, `create_time`, `update_time`) VALUES
('小心心', '/gifts/heart.png', '/gifts/heart_anim.gif', 1, 1, 1, 0, '表达你的心意', 1, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('啤酒', '/gifts/beer.png', '/gifts/beer_anim.gif', 10, 1, 1, 0, '请主播喝一杯', 2, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('跑车', '/gifts/car.png', '/gifts/car_anim.gif', 1000, 2, 2, 0, '豪华跑车礼物', 3, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('火箭', '/gifts/rocket.png', '/gifts/rocket_anim.gif', 5000, 2, 3, 0, '超级火箭升空', 4, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('城堡', '/gifts/castle.png', '/gifts/castle_anim.gif', 10000, 3, 3, 0, '梦幻城堡', 5, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000);

-- 插入默认用户等级配置
INSERT INTO `user_level_config` (`level`, `level_name`, `required_experience`, `level_icon`, `level_color`, `daily_message_limit`, `daily_gift_limit`, `color_privilege`, `enter_effect`, `status`, `create_time`, `update_time`) VALUES
(1, '新手', 0, '/levels/level1.png', '#999999', 100, 1000, 0, 0, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
(2, '入门', 100, '/levels/level2.png', '#66CCFF', 200, 5000, 0, 0, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
(3, '进阶', 500, '/levels/level3.png', '#3399FF', 300, 10000, 1, 0, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
(4, '高手', 2000, '/levels/level4.png', '#FF9933', 500, 50000, 1, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
(5, '达人', 10000, '/levels/level5.png', '#FF6600', 1000, 100000, 1, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
(6, '精英', 50000, '/levels/level6.png', '#FF3366', 2000, 500000, 1, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
(7, '大师', 200000, '/levels/level7.png', '#CC00FF', 5000, 1000000, 1, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
(8, '宗师', 1000000, '/levels/level8.png', '#FF0000', 10000, 5000000, 1, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000);

-- =====================================================
-- 完成提示
-- =====================================================
SELECT '✅ 数据库初始化完成！' AS message;
