/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80040
 Source Host           : localhost:3306
 Source Schema         : open-api

 Target Server Type    : MySQL
 Target Server Version : 80040
 File Encoding         : 65001

 Date: 06/04/2025 14:12:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `name` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `status` tinyint NULL DEFAULT 1 COMMENT '应用状态：0：禁用；1：开启',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_name`(`name` ASC) USING BTREE COMMENT '应用名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of application
-- ----------------------------
INSERT INTO `application` VALUES (1, 1, '管理员应用', 1, '2024-04-27 16:51:44', '2024-05-03 17:32:22');
INSERT INTO `application` VALUES (14, 2, '文学测试', 1, '2024-05-06 00:22:28', '2024-05-21 14:20:59');
INSERT INTO `application` VALUES (15, 8, '元庆测试', 1, '2024-05-08 16:46:21', '2024-05-21 13:46:25');
INSERT INTO `application` VALUES (16, 18, '游客测试', 1, '2024-05-18 23:09:53', '2025-03-21 10:35:26');
INSERT INTO `application` VALUES (17, 21, 'test', 1, '2024-05-20 21:18:37', '2024-05-20 21:18:37');
INSERT INTO `application` VALUES (18, 22, 'qry', 1, '2024-05-20 23:40:53', '2024-05-20 23:40:53');
INSERT INTO `application` VALUES (19, 27, '测试玩玩', 1, '2024-05-21 18:51:35', '2024-05-21 18:51:35');
INSERT INTO `application` VALUES (20, 28, 'test1', 1, '2024-05-21 18:56:35', '2024-05-21 18:56:35');
INSERT INTO `application` VALUES (21, 29, 'test2', 1, '2024-05-21 18:58:08', '2024-05-21 18:58:08');
INSERT INTO `application` VALUES (22, 2, '测试应用', 1, '2024-05-22 10:04:03', '2024-05-22 10:04:03');
INSERT INTO `application` VALUES (23, 27, '测试调用', 1, '2024-05-22 14:32:57', '2024-05-23 17:05:54');
INSERT INTO `application` VALUES (24, 2, '数据服务', 1, '2024-05-22 17:29:21', '2024-11-20 19:57:38');

-- ----------------------------
-- Table structure for application_key
-- ----------------------------
DROP TABLE IF EXISTS `application_key`;
CREATE TABLE `application_key`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `application_id` int NULL DEFAULT NULL COMMENT '应用id',
  `key_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'key名称',
  `access_key` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `secret_key` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `line` bigint NULL DEFAULT 20 COMMENT '额度：默认10',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '应用与key联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of application_key
-- ----------------------------
INSERT INTO `application_key` VALUES (1, 1, 'admin', '645afd75e5037c3e21c50887cae0914a', '80afae201db7ed5a8388150e7ebc573d', 2035, '2024-04-29 13:14:35', '2025-04-06 12:52:03');
INSERT INTO `application_key` VALUES (2, 14, 'swx', 'ae188edf64ce7a8819089064bb4aa305', 'a71722841bc2705ac07c27e6a5125305', 224, '2024-05-06 00:22:36', '2025-04-06 12:52:42');
INSERT INTO `application_key` VALUES (4, 15, 'pyq', '409b850578cd96bd6ace8c0b626a4c9b', '504ec43cb7d7fdcae92b2dfdcfdedb9e', 45, '2024-05-08 16:46:29', '2024-05-18 10:45:09');
INSERT INTO `application_key` VALUES (6, 14, 'sixkey', '06accaefeb51f242333e09cd6f7b7daa', 'e976f7d6c23df7d54e90e176c03acf3a', 212, '2024-05-18 00:51:14', '2025-04-06 13:01:24');
INSERT INTO `application_key` VALUES (7, 16, 'youke', '8ca9cbc448ac3f8c0f03e9e424d57e8b', '1c7e238bbdc1a0038d07e9efff598e28', 120, '2024-05-18 23:10:02', '2024-06-08 09:42:21');
INSERT INTO `application_key` VALUES (8, 17, 'dee', '2a3fe388cd6c3527cf9e79ac5b1a2027', '155c215d950c9f0129895e2cdf83eba4', 20, '2024-05-20 21:18:58', '2024-05-20 21:18:58');
INSERT INTO `application_key` VALUES (9, 18, 'servsdsdd', 'fdc308c5a484222f19fb20be5f5765f5', '5f0de26c2b79ad7480e8ee8f78a004fb', 17, '2024-05-20 23:41:09', '2024-05-20 23:43:35');
INSERT INTO `application_key` VALUES (10, 19, 'play', 'b730c5a0904b3c01ba0e2b79656dfb21', '68f845ed46fca1d262f942edc62106f3', 18, '2024-05-21 18:51:43', '2024-05-22 14:45:06');
INSERT INTO `application_key` VALUES (11, 20, 'test', '1593dd775d8e3c45864494356dcc2448', '3706fe5b5de7ac5edc35feb5ddc67802', 19, '2024-05-21 18:56:46', '2024-05-21 18:57:18');
INSERT INTO `application_key` VALUES (12, 21, 'test2', 'be399fce2c6683635c526c22053ca4fb', '3800c682e867515fcae59434b08a056d', 19, '2024-05-21 18:58:20', '2024-05-21 18:58:50');
INSERT INTO `application_key` VALUES (13, 14, 'demo', '1b3d11bd406d95d405e09e530b158905', 'd6e4f6681b6bf46c0c5c23c6e8e20aec', 220, '2024-05-22 13:57:47', '2025-04-06 13:02:43');
INSERT INTO `application_key` VALUES (14, 19, 'qixiang', 'f00c0e2bd2c0a9dac4f14917a7499228', 'bab6d8b30807ff9b603dc5c8e014d795', 20, '2024-05-22 14:35:31', '2024-05-22 14:35:31');
INSERT INTO `application_key` VALUES (15, 24, 'zhangsan', '131b1184623eeaad9cb1a6f59b607f85', '8264daa471b26fa842fe650376f12fb2', 108, '2024-05-22 17:29:37', '2025-04-06 13:51:20');
INSERT INTO `application_key` VALUES (16, 23, '23456uhgrthn', '560a388f1d32f9bec45c83722802d790', 'f8c9c74cd27a5b06d435b2d999861488', 17, '2024-05-23 16:39:21', '2025-04-06 13:52:11');

-- ----------------------------
-- Table structure for interface_info
-- ----------------------------
DROP TABLE IF EXISTS `interface_info`;
CREATE TABLE `interface_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '接口创建者id',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  `url` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '接口地址',
  `params` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `method` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '接口请求类型：如：post;get;put;delete',
  `number` bigint NULL DEFAULT 0 COMMENT '接口调用次数统计',
  `Line` bigint NULL DEFAULT 100 COMMENT '接口额度设置',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '接口描述',
  `request_header` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '请求头',
  `response_header` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '响应头',
  `status` int NULL DEFAULT 0 COMMENT '接口状态：0：下线；1：发布',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '接口信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interface_info
-- ----------------------------
INSERT INTO `interface_info` VALUES (8, 1, 'queryICPInfo', 'http://localhost:9000/provider/icp/info', '{\n   \"domainName\":\"string\"\n}', 'GET', 23, 100, 'ICP备案信息查询', '{\n  \"Content-Type\":\"application/json\"\n}', '{\n  \"Content-Type\":\"application/json\"\n}', 1, '2024-05-17 17:09:02', '2024-05-18 02:04:27');
INSERT INTO `interface_info` VALUES (9, 1, 'queryIpInfo', 'http://localhost:9000/provider/query/ipInfo', '{\n   \"ip\":\"string\"\n}', 'POST', 7, 100, 'IP信息获取:【ip测试用例：156.236.184.177】', '{\n  \"Content-Type\":\"application/json\"\n}', '{\n  \"Content-Type\":\"application/json\"\n}', 1, '2024-05-17 22:51:14', '2024-05-18 02:02:07');
INSERT INTO `interface_info` VALUES (10, 1, 'queryUrlQrCode', 'http://localhost:9000/provider/query/url/qrcode', '{\n     \"urlValue\":\"string\"\n}', 'POST', 3, 100, 'url地址转二维码:【url测试用例：https://www.baidu.com】', '{\n  \"Content-Type\":\"application/json\"\n}', '{\n  \"Content-Type\":\"application/json\"\n}', 1, '2024-05-17 23:32:05', '2024-05-18 01:06:21');
INSERT INTO `interface_info` VALUES (11, 1, 'queryQqInfo', 'http://localhost:9000/provider/query/qqInfo', '{\n     \"qq\":\"string\"\n}', 'POST', 32, 100, 'QQ信息获取', '{\n  \"Content-Type\":\"application/json\"\n}', '{\n  \"Content-Type\":\"application/json\"\n}', 1, '2024-05-17 23:45:07', '2025-04-06 13:51:20');
INSERT INTO `interface_info` VALUES (12, 1, 'queryCityWeatherInfo', 'http://localhost:9000/provider/query/city/weather', '{\n      \"city\":\"string\"\n}', 'POST', 8, 100, '城市天气查询', '{\n  \"Content-Type\":\"application/json\"\n}', '{\n  \"Content-Type\":\"application/json\"\n}', 1, '2024-05-18 10:24:10', '2025-03-21 10:40:14');
INSERT INTO `interface_info` VALUES (13, 1, 'charQuantityDetection', 'http://localhost:9000/provider/detection/char/count', '{\n   \"text\":\"string\"\n}', 'POST', 3, 100, '字符数量检测', '{\n  \"Content-Type\":\"application/json\"\n}', '{\n  \"Content-Type\":\"application/json\"\n}', 1, '2024-05-18 10:39:38', '2025-03-21 10:39:53');
INSERT INTO `interface_info` VALUES (14, 1, 'sensitiveWordsDetection', 'http://localhost:9000/provider/detection/sensitive/words', '{\n  \"text\":\"string\"\n}', 'POST', 2, 100, '敏感词检测', '{\n  \"Content-Type\":\"application/json\"\n}', '{\n  \"Content-Type\":\"application/json\"\n}', 1, '2024-05-18 10:54:00', '2025-03-21 10:39:23');
INSERT INTO `interface_info` VALUES (15, 1, 'chineseEnglishTranslation', 'http://localhost:9000/provider/chinese/translation/to/english', '{\n   \"text\":\"string\"\n}', 'POST', 22, 100, '中英互译', '{\n  \"Content-Type\":\"application/json\"\n}', '{\n  \"Content-Type\":\"application/json\"\n}', 1, '2024-05-18 11:02:41', '2025-04-06 13:52:11');

-- ----------------------------
-- Table structure for ip_white
-- ----------------------------
DROP TABLE IF EXISTS `ip_white`;
CREATE TABLE `ip_white`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `key_id` int NULL DEFAULT NULL,
  `key_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = 'ip白名单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ip_white
-- ----------------------------
INSERT INTO `ip_white` VALUES (1, 1, 'admin', '127.0.0.1', '2024-05-16 00:15:27', '2024-05-16 00:15:27');
INSERT INTO `ip_white` VALUES (6, 2, 'swx', '127.0.0.1', '2024-05-16 00:22:03', '2024-05-16 00:22:03');
INSERT INTO `ip_white` VALUES (7, 4, 'pyq', '127.0.0.1', '2024-05-16 00:38:51', '2024-05-16 00:38:51');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `parent_id` int NULL DEFAULT NULL COMMENT '菜单父类id',
  `path` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '路由path',
  `type` tinyint NULL DEFAULT NULL COMMENT '0：目录；1：菜单；2：按钮',
  `button` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '按钮操作权限',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单标题属性',
  `sort` tinyint NULL DEFAULT NULL COMMENT '菜单序号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0：不可用；1：可用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (2, 'Permission', 0, '/permission', 0, NULL, '权限管理', 1, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (3, 'User', 2, '/permission/user', 1, NULL, '用户管理', 2, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (4, 'Role', 2, '/permission/role', 1, NULL, '角色管理', 2, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (5, 'Menu', 2, '/permission/menu', 1, NULL, '菜单管理', 2, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (14, NULL, 3, '', 2, 'btn:user:add', '添加用户', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (15, NULL, 3, NULL, 2, 'btn:user:remove', '删除用户', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (16, NULL, 3, NULL, 2, 'btn:user:update', '修改用户', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (17, NULL, 3, NULL, 2, 'btn:user:list', '查看用户', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (20, NULL, 4, NULL, 2, 'btn:role:add', '添加角色', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (21, NULL, 4, NULL, 2, 'btn:role:update', '修改角色', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (22, NULL, 4, NULL, 2, 'btn:role:list', '查看角色', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (23, NULL, 4, NULL, 2, 'btn:role:remove', '删除角色', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (24, NULL, 5, NULL, 2, 'btn:menu:add', '添加菜单', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (25, NULL, 5, NULL, 2, 'btn:menu:update', '修改菜单', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (26, NULL, 5, NULL, 2, 'btn:menu:list', '查看菜单', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (27, NULL, 5, NULL, 2, 'btn:menu:remove', '删除菜单', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (30, NULL, 3, NULL, 2, 'btn:user:per:role', '分配角色', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (31, NULL, 4, NULL, 2, 'btn:role:per', '分配权限', 3, 1, '2024-04-26 14:11:55', '2024-04-26 14:11:55');
INSERT INTO `menu` VALUES (34, 'Interface', 0, '/interface', 0, NULL, '接口管理', 1, 1, '2024-04-26 15:23:54', '2024-04-26 15:23:54');
INSERT INTO `menu` VALUES (37, 'List', 34, '/interface/list', 1, NULL, '接口列表', 2, 1, '2024-04-26 16:05:06', '2024-04-26 16:05:06');
INSERT INTO `menu` VALUES (48, 'AppManage', 0, '/appmanage', 0, NULL, '应用管理', 1, 1, '2024-04-27 13:23:07', '2024-05-12 21:46:58');
INSERT INTO `menu` VALUES (49, 'Application', 48, '/appmanage/application', 1, NULL, '我的应用', 2, 1, '2024-04-27 13:23:26', '2024-05-12 21:50:36');
INSERT INTO `menu` VALUES (50, 'Message', 48, '/appmanage/message', 1, NULL, '消息管理', 2, 1, '2024-04-27 13:23:45', '2024-05-12 21:50:10');
INSERT INTO `menu` VALUES (55, NULL, 37, NULL, 2, 'btn:interface:invoke', '接口调用操作', 3, 1, '2024-05-02 23:53:53', '2024-05-02 23:53:53');
INSERT INTO `menu` VALUES (56, NULL, 49, NULL, 2, 'btn:application:create', '创建应用操作', 3, 1, '2024-05-02 23:58:18', '2024-05-02 23:58:18');
INSERT INTO `menu` VALUES (57, NULL, 49, NULL, 2, 'btn:application:open', '应用开启操作', 3, 1, '2024-05-02 23:58:57', '2024-05-02 23:58:57');
INSERT INTO `menu` VALUES (58, NULL, 49, NULL, 2, 'btn:application:update', '应用修改操作', 3, 1, '2024-05-02 23:59:29', '2024-05-02 23:59:29');
INSERT INTO `menu` VALUES (59, NULL, 49, NULL, 2, 'btn:application:delete', '应用删除操作', 3, 1, '2024-05-02 23:59:45', '2024-05-02 23:59:45');
INSERT INTO `menu` VALUES (60, NULL, 49, NULL, 2, 'btn:application:addkey', '添加key操作', 3, 1, '2024-05-03 00:00:31', '2024-05-03 00:00:31');
INSERT INTO `menu` VALUES (61, NULL, 49, NULL, 2, 'btn:application:key:delete', 'key删除操作', 3, 1, '2024-05-03 00:01:21', '2024-05-03 00:01:21');
INSERT INTO `menu` VALUES (62, NULL, 49, NULL, 2, 'btn:application:key:line', '查看key配额操作', 3, 1, '2024-05-03 00:05:22', '2024-05-03 00:05:22');
INSERT INTO `menu` VALUES (64, 'Traffic', 34, '/interface/traffic', 1, NULL, '流量详情', 2, 1, '2024-05-03 14:51:51', '2024-05-03 14:51:51');
INSERT INTO `menu` VALUES (65, 'Switch', 34, '/interface/switch', 1, NULL, '接口开关', 2, 1, '2024-05-03 17:51:40', '2024-05-03 17:51:40');
INSERT INTO `menu` VALUES (66, NULL, 65, NULL, 2, 'btn:interface:delete', '接口删除操作', 3, 1, '2024-05-03 18:00:45', '2024-05-03 18:00:45');
INSERT INTO `menu` VALUES (67, NULL, 65, NULL, 2, 'btn:interface:update', '接口修改操作', 3, 1, '2024-05-03 18:00:59', '2024-05-03 18:00:59');
INSERT INTO `menu` VALUES (68, NULL, 65, NULL, 2, 'btn:interface:publish', '接口发布操作', 3, 1, '2024-05-03 18:01:10', '2024-05-03 18:01:10');
INSERT INTO `menu` VALUES (69, NULL, 65, NULL, 2, 'btn:interface:add', '接口新增操作', 3, 1, '2024-05-03 18:01:25', '2024-05-03 18:01:25');
INSERT INTO `menu` VALUES (70, NULL, 65, NULL, 2, 'btn:interface:invoke', '接口调用操作', 3, 1, '2024-05-03 18:01:49', '2024-05-03 18:01:49');
INSERT INTO `menu` VALUES (73, 'Lines', 0, '/lines', 0, NULL, '配额管理', 1, 1, '2024-05-03 23:49:16', '2024-05-03 23:49:16');
INSERT INTO `menu` VALUES (74, 'Order', 73, '/lines/order', 1, '', '额度充值', 2, 1, '2024-05-03 23:49:32', '2024-05-03 23:49:32');
INSERT INTO `menu` VALUES (75, 'Setting', 73, '/lines/setting', 1, NULL, '配额设置', 2, 1, '2024-05-05 12:36:29', '2024-05-05 12:36:29');
INSERT INTO `menu` VALUES (76, 'OrderList', 73, '/lines/orderList', 1, NULL, '订单列表', 2, 1, '2024-05-06 00:01:57', '2024-05-06 00:01:57');
INSERT INTO `menu` VALUES (90, NULL, 74, NULL, 2, 'btn:plan:buy', '购买额度', 3, 1, '2024-05-12 21:31:19', '2024-05-12 21:31:19');
INSERT INTO `menu` VALUES (91, NULL, 75, NULL, 2, 'btn:plan:setting:add', '新增套餐', 3, 1, '2024-05-12 21:33:09', '2024-05-12 21:33:09');
INSERT INTO `menu` VALUES (92, NULL, 75, NULL, 2, 'btn:plan:setting:update', '修改额度套餐', 3, 1, '2024-05-12 21:33:58', '2024-05-12 21:33:58');
INSERT INTO `menu` VALUES (93, NULL, 75, NULL, 2, 'btn:plan:setting:remove', '删除额度套餐', 3, 1, '2024-05-12 21:34:17', '2024-05-12 21:34:17');
INSERT INTO `menu` VALUES (94, NULL, 76, NULL, 2, 'btn:order:update', '修改订单数据', 3, 1, '2024-05-12 23:32:06', '2024-05-12 23:32:06');
INSERT INTO `menu` VALUES (95, NULL, 76, NULL, 2, 'btn:order:remove', '删除订单数据', 3, 1, '2024-05-12 23:32:21', '2024-05-12 23:32:21');
INSERT INTO `menu` VALUES (96, NULL, 76, NULL, 2, 'btn:order:list', '查询订单列表', 3, 1, '2024-05-12 23:38:13', '2024-05-12 23:38:13');
INSERT INTO `menu` VALUES (97, 'About', 0, '/', 0, NULL, '个人中心', 1, 1, '2024-05-16 14:14:36', '2024-05-16 14:14:36');
INSERT INTO `menu` VALUES (99, 'My', 97, '/about', 1, NULL, '个人中心', 2, 1, '2024-05-16 14:16:52', '2024-05-16 14:16:52');
INSERT INTO `menu` VALUES (100, NULL, 75, NULL, 2, 'btn:plan:setting:list', '额度查询', 3, 1, '2024-05-21 15:41:44', '2024-05-21 15:41:44');

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '订单id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户Id',
  `plan_id` int NULL DEFAULT NULL COMMENT '套餐id',
  `access_key` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'accessKey',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单金额',
  `pay_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '支付方式',
  `status` tinyint NULL DEFAULT 0 COMMENT '支付状态：0：未支付；1：已支付',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '订单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES (3, '96299fa49b0b4aac9c05dd227e021fa5', 2, 9, 'ae188edf64ce7a8819089064bb4aa305', 0.01, 'wxpay', 1, '2024-05-06 23:07:33', '2024-05-13 10:28:29');
INSERT INTO `order_info` VALUES (6, 'd2816d8822044a73a4657f9ff08c2ea3', 2, 9, 'ae188edf64ce7a8819089064bb4aa305', 100.00, 'wxpay', 0, '2024-05-06 23:21:42', '2024-05-13 10:34:30');
INSERT INTO `order_info` VALUES (7, '1734e6ab65474d13b625f2568367b1c5', 18, 9, '8ca9cbc448ac3f8c0f03e9e424d57e8b', 0.01, 'wxpay', 1, '2024-05-19 00:03:41', '2024-05-19 00:04:04');
INSERT INTO `order_info` VALUES (8, '79a0f0c57ef74a8f974f3cfc4f483c62', 18, 9, '8ca9cbc448ac3f8c0f03e9e424d57e8b', 0.01, 'wxpay', 1, '2024-05-19 00:09:13', '2024-05-19 00:09:37');
INSERT INTO `order_info` VALUES (9, '60f722e6a31b442899142be53dbcc9c4', 18, 9, '8ca9cbc448ac3f8c0f03e9e424d57e8b', 0.01, 'wxpay', 1, '2024-05-19 00:15:37', '2024-05-19 00:15:56');
INSERT INTO `order_info` VALUES (10, '8a5bf95ba8764f30acea22f7faeb8656', 2, 9, 'ae188edf64ce7a8819089064bb4aa305', 0.01, 'wxpay', 0, '2024-05-19 17:07:01', '2024-05-19 17:07:01');
INSERT INTO `order_info` VALUES (11, 'ce5915f766614799ba08c482548a092f', 2, 9, '06accaefeb51f242333e09cd6f7b7daa', 0.01, 'wxpay', 0, '2024-05-20 23:01:37', '2024-05-20 23:01:37');
INSERT INTO `order_info` VALUES (12, 'fc47e16846834b5096e1da466fb001e8', 2, 9, '06accaefeb51f242333e09cd6f7b7daa', 0.01, 'wxpay', 0, '2024-05-20 23:04:49', '2024-05-20 23:04:49');
INSERT INTO `order_info` VALUES (13, '6ca4cf8d05f547089615135378471b17', 2, 9, '06accaefeb51f242333e09cd6f7b7daa', 0.01, 'wxpay', 0, '2024-05-20 23:08:00', '2024-05-20 23:08:00');
INSERT INTO `order_info` VALUES (14, 'df479c9deb1744cfac755e1633ce9016', 2, 9, '06accaefeb51f242333e09cd6f7b7daa', 0.01, 'wxpay', 0, '2024-05-20 23:09:38', '2024-05-20 23:09:38');
INSERT INTO `order_info` VALUES (15, 'b6ec3fa58af0452db6836c2d4de4ac44', 22, 9, 'fdc308c5a484222f19fb20be5f5765f5', 0.01, 'alipay', 0, '2024-05-20 23:41:35', '2024-05-20 23:41:35');
INSERT INTO `order_info` VALUES (16, 'b1015796e99d45c1a5f30a7f3b7ad665', 2, 9, 'ae188edf64ce7a8819089064bb4aa305', 0.01, 'wxpay', 0, '2024-05-21 15:14:02', '2024-05-21 15:14:02');
INSERT INTO `order_info` VALUES (17, 'd3b5a2655e984acfb50ff2024b837ef8', 27, 9, 'b730c5a0904b3c01ba0e2b79656dfb21', 0.01, 'wxpay', 0, '2024-05-22 10:03:41', '2024-05-22 10:03:41');
INSERT INTO `order_info` VALUES (18, 'a31a1c951ae541058d0c76cb6e239314', 27, 8, 'f00c0e2bd2c0a9dac4f14917a7499228', 0.02, 'alipay', 0, '2024-05-22 14:41:41', '2024-05-22 14:41:41');
INSERT INTO `order_info` VALUES (19, '54917b7fa61049e89028a1ba37e5667f', 2, 9, 'ae188edf64ce7a8819089064bb4aa305', 0.01, 'wxpay', 0, '2024-05-22 17:38:31', '2024-05-22 17:38:31');
INSERT INTO `order_info` VALUES (20, '9cc5b2bbaf0c42c1958e2ffa2bb90b43', 2, 9, 'ae188edf64ce7a8819089064bb4aa305', 0.01, 'alipay', 0, '2024-05-23 16:06:28', '2024-05-23 16:06:28');
INSERT INTO `order_info` VALUES (21, '6df8b91fecef4fb899aeea087c3d72fa', 2, 9, 'ae188edf64ce7a8819089064bb4aa305', 0.01, 'alipay', 0, '2024-05-23 16:06:52', '2024-05-23 16:06:52');
INSERT INTO `order_info` VALUES (22, '013196f0ba3740f1836378f9b775c696', 27, 9, 'b730c5a0904b3c01ba0e2b79656dfb21', 0.01, 'wxpay', 0, '2024-05-23 17:42:53', '2024-05-23 17:42:53');
INSERT INTO `order_info` VALUES (23, '477837e4d2254405ad540577916ce03b', 2, 5, 'ae188edf64ce7a8819089064bb4aa305', 0.03, 'alipay', 0, '2024-05-23 21:58:57', '2024-05-23 21:58:57');
INSERT INTO `order_info` VALUES (24, 'e4c1348a7a474abe93e5fef84982c7a4', 1, 9, '645afd75e5037c3e21c50887cae0914a', 0.01, 'alipay', 0, '2024-08-03 19:25:40', '2024-08-03 19:25:40');
INSERT INTO `order_info` VALUES (25, 'd1c983f0c8824d93b95967d16835be50', 1, 8, '645afd75e5037c3e21c50887cae0914a', 0.02, 'alipay', 0, '2025-03-19 20:26:54', '2025-03-19 20:26:54');
INSERT INTO `order_info` VALUES (28, '1dff4ba5549f4bd3ac757d70271bb165', 2, 9, '06accaefeb51f242333e09cd6f7b7daa', 0.01, 'aliPay', 1, '2025-03-25 20:57:40', '2025-03-25 20:57:42');
INSERT INTO `order_info` VALUES (29, 'af04a3e3f4d048fb9d00c4e743d0815b', 2, 9, '1b3d11bd406d95d405e09e530b158905', 0.01, 'aliPay', 0, '2025-03-25 21:14:45', '2025-03-25 21:14:45');
INSERT INTO `order_info` VALUES (30, '31259e37fe27447a8540f8d351298ae1', 2, 9, '1b3d11bd406d95d405e09e530b158905', 0.01, 'aliPay', 0, '2025-03-25 21:16:06', '2025-03-25 21:16:06');
INSERT INTO `order_info` VALUES (31, 'fe3f29f3d38345f6a9000bcd335f45b8', 2, 9, '1b3d11bd406d95d405e09e530b158905', 0.01, 'aliPay', 0, '2025-03-25 21:17:05', '2025-03-25 21:17:05');
INSERT INTO `order_info` VALUES (33, 'bb24e2e98bb745e6b6f0e3353b1cafef', 2, 9, '131b1184623eeaad9cb1a6f59b607f85', 0.01, 'aliPay', 0, '2025-03-25 21:20:19', '2025-03-25 21:20:19');
INSERT INTO `order_info` VALUES (34, 'b792b403888347f281cc9a3a986de69f', 2, 9, '131b1184623eeaad9cb1a6f59b607f85', 0.01, 'aliPay', 0, '2025-03-25 21:21:16', '2025-03-25 21:21:16');
INSERT INTO `order_info` VALUES (35, '3afa4197447b46059294c433f28a9b23', 2, 9, '131b1184623eeaad9cb1a6f59b607f85', 0.01, 'aliPay', 0, '2025-03-25 21:23:05', '2025-03-25 21:23:05');
INSERT INTO `order_info` VALUES (36, '88658832305f41c9ac33ca9cb1e5554e', 2, 9, '131b1184623eeaad9cb1a6f59b607f85', 0.01, 'aliPay', 0, '2025-03-25 21:23:53', '2025-03-25 21:23:53');
INSERT INTO `order_info` VALUES (37, 'c6069ec575b34b7fb4e77369ac97395d', 2, 9, '131b1184623eeaad9cb1a6f59b607f85', 0.01, 'aliPay', 1, '2025-03-25 21:24:10', '2025-03-25 21:24:46');
INSERT INTO `order_info` VALUES (38, '45ebdf249ccc4dd484014c2d82c6c2f9', 2, 9, '1b3d11bd406d95d405e09e530b158905', 0.01, 'aliPay', 0, '2025-03-25 21:25:00', '2025-03-25 21:25:00');
INSERT INTO `order_info` VALUES (39, 'c5758be8311d4f9aa16a7039f923461a', 2, 9, '1b3d11bd406d95d405e09e530b158905', 0.01, 'aliPay', 0, '2025-03-25 21:27:09', '2025-03-25 21:27:09');
INSERT INTO `order_info` VALUES (40, 'd29579a781654ac7b1dc372aee016840', 2, 9, '1b3d11bd406d95d405e09e530b158905', 0.01, 'aliPay', 0, '2025-03-25 21:27:28', '2025-03-25 21:27:28');
INSERT INTO `order_info` VALUES (41, '3d5df4e613a2455a9562a732baf35ff8', 1, 9, '645afd75e5037c3e21c50887cae0914a', 0.01, 'aliPay', 1, '2025-04-06 12:52:03', '2025-04-06 12:52:03');
INSERT INTO `order_info` VALUES (42, 'eaebfda56fa04eae96c7470c8c3d16d1', 2, 8, 'ae188edf64ce7a8819089064bb4aa305', 0.02, 'aliPay', 1, '2025-04-06 12:52:42', '2025-04-06 12:52:42');
INSERT INTO `order_info` VALUES (43, 'c1088978cb48420b93e464ba9fd012ae', 2, 9, '1b3d11bd406d95d405e09e530b158905', 0.01, 'wxPay', 1, '2025-04-06 12:54:13', '2025-04-06 12:54:13');
INSERT INTO `order_info` VALUES (44, '52ef935227914a50ae64110c25210a11', 2, 9, '06accaefeb51f242333e09cd6f7b7daa', 0.01, 'aliPay', 1, '2025-04-06 13:01:24', '2025-04-06 13:01:24');
INSERT INTO `order_info` VALUES (45, 'e8948ee18a03451f9855c24615a21649', 2, 9, '1b3d11bd406d95d405e09e530b158905', 0.01, 'wxPay', 1, '2025-04-06 13:02:43', '2025-04-06 13:02:43');

-- ----------------------------
-- Table structure for plan
-- ----------------------------
DROP TABLE IF EXISTS `plan`;
CREATE TABLE `plan`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '套餐名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '套餐价格',
  `unit` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '套餐单位',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '额度套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of plan
-- ----------------------------
INSERT INTO `plan` VALUES (3, '套餐1', 0.04, '/400次', '2024-05-05 14:18:00', '2024-05-18 22:54:39');
INSERT INTO `plan` VALUES (4, '套餐2', 0.05, '/500次', '2024-05-05 14:18:00', '2024-05-18 22:54:12');
INSERT INTO `plan` VALUES (5, '套餐3', 0.03, '/300次', '2024-05-05 14:18:00', '2024-05-18 22:54:05');
INSERT INTO `plan` VALUES (6, '套餐4', 0.06, '/600次', '2024-05-05 15:40:00', '2024-05-18 22:53:57');
INSERT INTO `plan` VALUES (7, '套餐5', 0.07, '/700次', '2024-05-05 15:40:00', '2024-05-18 22:53:51');
INSERT INTO `plan` VALUES (8, '套餐6', 0.02, '/200次', '2024-05-05 15:41:00', '2024-05-18 22:53:26');
INSERT INTO `plan` VALUES (9, '套餐7', 0.01, '/100次', '2024-05-05 15:41:53', '2024-05-05 21:22:44');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `sort` tinyint NULL DEFAULT NULL COMMENT '角色排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员', 1, '2024-04-26 14:46:52', '2024-04-26 14:46:52');
INSERT INTO `role` VALUES (8, '普通用户', 2, '2024-05-02 23:52:52', '2024-05-02 23:52:52');
INSERT INTO `role` VALUES (14, '测试', 3, '2024-05-21 15:39:22', '2024-05-21 15:39:22');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 278 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色菜单连表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (174, 8, 97, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (175, 8, 99, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (176, 8, 96, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (177, 8, 74, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (178, 8, 90, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (179, 8, 48, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (180, 8, 50, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (181, 8, 49, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (182, 8, 62, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (183, 8, 61, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (184, 8, 60, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (185, 8, 59, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (186, 8, 58, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (187, 8, 57, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (188, 8, 56, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (189, 8, 64, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (190, 8, 37, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (191, 8, 55, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (192, 8, 73, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (193, 8, 76, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (194, 8, 34, '2024-05-16 14:55:14', '2024-05-16 14:55:14');
INSERT INTO `role_menu` VALUES (246, 14, 97, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (247, 14, 99, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (248, 14, 96, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (249, 14, 100, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (250, 14, 74, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (251, 14, 90, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (252, 14, 48, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (253, 14, 50, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (254, 14, 49, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (255, 14, 62, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (256, 14, 61, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (257, 14, 60, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (258, 14, 59, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (259, 14, 58, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (260, 14, 57, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (261, 14, 56, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (262, 14, 70, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (263, 14, 64, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (264, 14, 37, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (265, 14, 55, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (266, 14, 26, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (267, 14, 22, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (268, 14, 17, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (269, 14, 73, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (270, 14, 76, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (271, 14, 75, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (272, 14, 34, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (273, 14, 65, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (274, 14, 2, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (275, 14, 5, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (276, 14, 4, '2024-05-21 15:43:06', '2024-05-21 15:43:06');
INSERT INTO `role_menu` VALUES (277, 14, 3, '2024-05-21 15:43:06', '2024-05-21 15:43:06');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `gender` tinyint NULL DEFAULT NULL COMMENT '0:女；1：男',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint NULL DEFAULT 1 COMMENT '0：禁用；1：启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, '18214376259', '2073958890@qq.com', 1, '2024-04-26 14:48:15', '2024-05-21 15:02:57');
INSERT INTO `user` VALUES (2, 'swx', 'e10adc3949ba59abbe56e057f20f883e', 1, '18687470579', '3168620057@qq.com', 1, '2024-04-26 14:48:15', '2024-05-19 17:18:07');
INSERT INTO `user` VALUES (27, 'test', 'e10adc3949ba59abbe56e057f20f883e', 1, '18695874589', '234654@qq.com', 1, '2024-05-21 18:49:46', '2024-05-21 18:49:46');
INSERT INTO `user` VALUES (28, 'test1', 'e10adc3949ba59abbe56e057f20f883e', 0, '13698457896', '13456@qq.com', 1, '2024-05-21 18:53:04', '2024-05-21 18:53:04');
INSERT INTO `user` VALUES (29, 'test2', 'e10adc3949ba59abbe56e057f20f883e', 1, '15969854789', '1598745@qq.com', 1, '2024-05-21 18:53:29', '2024-05-21 18:53:29');

-- ----------------------------
-- Table structure for user_interface_info
-- ----------------------------
DROP TABLE IF EXISTS `user_interface_info`;
CREATE TABLE `user_interface_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `interface_id` int NULL DEFAULT NULL COMMENT '接口id',
  `key_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '应用key的名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 252 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户调用接口次数信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_interface_info
-- ----------------------------
INSERT INTO `user_interface_info` VALUES (141, 1, 8, 'admin', '2024-05-17 17:11:12', '2024-05-17 17:11:12');
INSERT INTO `user_interface_info` VALUES (142, 1, 8, 'admin', '2024-05-17 17:20:06', '2024-05-17 17:20:06');
INSERT INTO `user_interface_info` VALUES (143, 1, 8, 'admin', '2024-05-17 17:25:28', '2024-05-17 17:25:28');
INSERT INTO `user_interface_info` VALUES (144, 1, 8, 'admin', '2024-05-17 17:26:01', '2024-05-17 17:26:01');
INSERT INTO `user_interface_info` VALUES (145, 1, 8, 'admin', '2024-05-17 17:38:13', '2024-05-17 17:38:13');
INSERT INTO `user_interface_info` VALUES (146, 1, 8, 'admin', '2024-05-17 17:40:46', '2024-05-17 17:40:46');
INSERT INTO `user_interface_info` VALUES (147, 1, 8, 'admin', '2024-05-17 17:41:35', '2024-05-17 17:41:35');
INSERT INTO `user_interface_info` VALUES (148, 1, 8, 'admin', '2024-05-17 17:44:20', '2024-05-17 17:44:20');
INSERT INTO `user_interface_info` VALUES (149, 1, 8, 'admin', '2024-05-17 17:52:56', '2024-05-17 17:52:56');
INSERT INTO `user_interface_info` VALUES (150, 1, 8, 'admin', '2024-05-17 17:57:55', '2024-05-17 17:57:55');
INSERT INTO `user_interface_info` VALUES (151, 1, 8, 'admin', '2024-05-17 22:11:52', '2024-05-17 22:11:52');
INSERT INTO `user_interface_info` VALUES (152, 1, 8, 'admin', '2024-05-17 22:18:25', '2024-05-17 22:18:25');
INSERT INTO `user_interface_info` VALUES (153, 1, 8, 'admin', '2024-05-17 22:23:49', '2024-05-17 22:23:49');
INSERT INTO `user_interface_info` VALUES (154, 1, 8, 'admin', '2024-05-17 22:25:07', '2024-05-17 22:25:07');
INSERT INTO `user_interface_info` VALUES (155, 1, 8, 'admin', '2024-05-17 22:25:23', '2024-05-17 22:25:23');
INSERT INTO `user_interface_info` VALUES (156, 1, 8, 'admin', '2024-05-17 22:25:28', '2024-05-17 22:25:28');
INSERT INTO `user_interface_info` VALUES (157, 1, 8, 'admin', '2024-05-17 22:27:41', '2024-05-17 22:27:41');
INSERT INTO `user_interface_info` VALUES (158, 1, 8, 'admin', '2024-05-17 22:29:15', '2024-05-17 22:29:15');
INSERT INTO `user_interface_info` VALUES (159, 1, 8, 'admin', '2024-05-17 22:29:25', '2024-05-17 22:29:25');
INSERT INTO `user_interface_info` VALUES (160, 1, 8, 'admin', '2024-05-17 22:30:23', '2024-05-17 22:30:23');
INSERT INTO `user_interface_info` VALUES (161, 1, 8, 'admin', '2024-05-17 22:30:27', '2024-05-17 22:30:27');
INSERT INTO `user_interface_info` VALUES (162, 1, 8, 'admin', '2024-05-17 22:30:31', '2024-05-17 22:30:31');
INSERT INTO `user_interface_info` VALUES (163, 1, 9, 'admin', '2024-05-17 22:53:24', '2024-05-17 22:53:24');
INSERT INTO `user_interface_info` VALUES (164, 1, 9, 'admin', '2024-05-17 22:57:02', '2024-05-17 22:57:02');
INSERT INTO `user_interface_info` VALUES (165, 1, 9, 'admin', '2024-05-17 22:57:45', '2024-05-17 22:57:45');
INSERT INTO `user_interface_info` VALUES (166, 1, 9, 'admin', '2024-05-17 22:59:24', '2024-05-17 22:59:24');
INSERT INTO `user_interface_info` VALUES (167, 1, 10, 'admin', '2024-05-17 23:32:48', '2024-05-17 23:32:48');
INSERT INTO `user_interface_info` VALUES (168, 1, 10, 'admin', '2024-05-17 23:33:25', '2024-05-17 23:33:25');
INSERT INTO `user_interface_info` VALUES (169, 1, 11, 'admin', '2024-05-17 23:45:43', '2024-05-17 23:45:43');
INSERT INTO `user_interface_info` VALUES (170, 1, 11, 'swx', '2024-05-17 23:46:08', '2024-05-17 23:46:08');
INSERT INTO `user_interface_info` VALUES (171, 1, 11, 'admin', '2024-05-18 00:15:20', '2024-05-18 00:15:20');
INSERT INTO `user_interface_info` VALUES (172, 1, 11, 'admin', '2024-05-18 00:16:16', '2024-05-18 00:16:16');
INSERT INTO `user_interface_info` VALUES (173, 1, 11, 'admin', '2024-05-18 00:19:46', '2024-05-18 00:19:46');
INSERT INTO `user_interface_info` VALUES (174, 1, 11, 'admin', '2024-05-18 00:22:11', '2024-05-18 00:22:11');
INSERT INTO `user_interface_info` VALUES (175, 1, 11, 'admin', '2024-05-18 00:22:37', '2024-05-18 00:22:37');
INSERT INTO `user_interface_info` VALUES (176, 1, 11, 'admin', '2024-05-18 00:22:41', '2024-05-18 00:22:41');
INSERT INTO `user_interface_info` VALUES (177, 1, 11, 'admin', '2024-05-18 00:24:50', '2024-05-18 00:24:50');
INSERT INTO `user_interface_info` VALUES (178, 1, 11, 'admin', '2024-05-18 00:26:16', '2024-05-18 00:26:16');
INSERT INTO `user_interface_info` VALUES (179, 1, 11, 'admin', '2024-05-18 00:28:11', '2024-05-18 00:28:11');
INSERT INTO `user_interface_info` VALUES (180, 1, 11, 'admin', '2024-05-18 00:28:21', '2024-05-18 00:28:21');
INSERT INTO `user_interface_info` VALUES (181, 1, 11, 'admin', '2024-05-18 00:29:16', '2024-05-18 00:29:16');
INSERT INTO `user_interface_info` VALUES (182, 1, 10, 'admin', '2024-05-18 00:33:45', '2024-05-18 00:33:45');
INSERT INTO `user_interface_info` VALUES (183, 1, 11, 'swx', '2024-05-18 00:44:40', '2024-05-18 00:44:40');
INSERT INTO `user_interface_info` VALUES (184, 1, 11, 'pyq', '2024-05-18 00:47:33', '2024-05-18 00:47:33');
INSERT INTO `user_interface_info` VALUES (185, 1, 11, 'swx', '2024-05-18 00:52:05', '2024-05-18 00:52:05');
INSERT INTO `user_interface_info` VALUES (186, 1, 11, 'sixkey', '2024-05-18 01:02:48', '2024-05-18 01:02:48');
INSERT INTO `user_interface_info` VALUES (187, 1, 11, 'sixkey', '2024-05-18 01:04:45', '2024-05-18 01:04:45');
INSERT INTO `user_interface_info` VALUES (188, 1, 10, 'sixkey', '2024-05-18 01:06:22', '2024-05-18 01:06:22');
INSERT INTO `user_interface_info` VALUES (189, 1, 9, 'admin', '2024-05-18 01:08:43', '2024-05-18 01:08:43');
INSERT INTO `user_interface_info` VALUES (190, 1, 9, 'sixkey', '2024-05-18 01:11:18', '2024-05-18 01:11:18');
INSERT INTO `user_interface_info` VALUES (191, 1, 9, 'admin', '2024-05-18 01:15:09', '2024-05-18 01:15:09');
INSERT INTO `user_interface_info` VALUES (192, 1, 11, 'admin', '2024-05-18 01:16:22', '2024-05-18 01:16:22');
INSERT INTO `user_interface_info` VALUES (193, 1, 11, 'admin', '2024-05-18 01:18:02', '2024-05-18 01:18:02');
INSERT INTO `user_interface_info` VALUES (194, 1, 11, 'admin', '2024-05-18 01:18:45', '2024-05-18 01:18:45');
INSERT INTO `user_interface_info` VALUES (195, 1, 11, 'admin', '2024-05-18 01:21:14', '2024-05-18 01:21:14');
INSERT INTO `user_interface_info` VALUES (196, 1, 11, 'admin', '2024-05-18 01:27:52', '2024-05-18 01:27:52');
INSERT INTO `user_interface_info` VALUES (197, 1, 9, 'admin', '2024-05-18 01:35:03', '2024-05-18 01:35:03');
INSERT INTO `user_interface_info` VALUES (198, 1, 8, 'admin', '2024-05-18 01:36:30', '2024-05-18 01:36:30');
INSERT INTO `user_interface_info` VALUES (199, 1, 8, 'pyq', '2024-05-18 01:37:10', '2024-05-18 01:37:10');
INSERT INTO `user_interface_info` VALUES (200, 1, 9, 'admin', '2024-05-18 02:02:07', '2024-05-18 02:02:07');
INSERT INTO `user_interface_info` VALUES (201, 1, 8, 'admin', '2024-05-18 02:03:28', '2024-05-18 02:03:28');
INSERT INTO `user_interface_info` VALUES (202, 1, 8, 'admin', '2024-05-18 02:04:27', '2024-05-18 02:04:27');
INSERT INTO `user_interface_info` VALUES (203, 1, 12, 'admin', '2024-05-18 10:27:32', '2024-05-18 10:27:32');
INSERT INTO `user_interface_info` VALUES (204, 1, 12, 'pyq', '2024-05-18 10:28:14', '2024-05-18 10:28:14');
INSERT INTO `user_interface_info` VALUES (205, 1, 13, 'admin', '2024-05-18 10:44:25', '2024-05-18 10:44:25');
INSERT INTO `user_interface_info` VALUES (206, 1, 13, 'pyq', '2024-05-18 10:45:09', '2024-05-18 10:45:09');
INSERT INTO `user_interface_info` VALUES (207, 1, 14, 'admin', '2024-05-18 10:54:35', '2024-05-18 10:54:35');
INSERT INTO `user_interface_info` VALUES (208, 1, 14, 'swx', '2024-05-18 10:54:54', '2024-05-18 10:54:54');
INSERT INTO `user_interface_info` VALUES (209, 1, 15, 'admin', '2024-05-18 11:03:26', '2024-05-18 11:03:26');
INSERT INTO `user_interface_info` VALUES (210, 1, 15, 'admin', '2024-05-18 11:04:04', '2024-05-18 11:04:04');
INSERT INTO `user_interface_info` VALUES (211, 2, 15, 'swx', '2024-05-18 11:12:23', '2024-05-18 11:12:23');
INSERT INTO `user_interface_info` VALUES (212, 1, 12, 'swx', '2024-05-18 11:44:02', '2024-05-18 11:44:02');
INSERT INTO `user_interface_info` VALUES (213, 1, 12, 'swx', '2024-05-18 11:44:11', '2024-05-18 11:44:11');
INSERT INTO `user_interface_info` VALUES (214, 18, 15, 'youke', '2024-05-18 23:16:05', '2024-05-18 23:16:05');
INSERT INTO `user_interface_info` VALUES (215, 2, 12, 'sixkey', '2024-05-19 14:22:19', '2024-05-19 14:22:19');
INSERT INTO `user_interface_info` VALUES (216, 2, 12, 'sixkey', '2024-05-19 14:22:56', '2024-05-19 14:22:56');
INSERT INTO `user_interface_info` VALUES (217, 2, 11, 'swx', '2024-05-19 17:08:51', '2024-05-19 17:08:51');
INSERT INTO `user_interface_info` VALUES (218, 2, 11, 'swx', '2024-05-19 17:11:01', '2024-05-19 17:11:01');
INSERT INTO `user_interface_info` VALUES (219, 2, 12, 'swx', '2024-05-20 20:26:05', '2024-05-20 20:26:05');
INSERT INTO `user_interface_info` VALUES (220, 2, 12, 'sixkey', '2024-05-20 21:07:35', '2024-05-20 21:07:35');
INSERT INTO `user_interface_info` VALUES (221, 22, 15, 'servsdsdd', '2024-05-20 23:42:45', '2024-05-20 23:42:45');
INSERT INTO `user_interface_info` VALUES (222, 22, 15, 'servsdsdd', '2024-05-20 23:43:25', '2024-05-20 23:43:25');
INSERT INTO `user_interface_info` VALUES (223, 22, 15, 'servsdsdd', '2024-05-20 23:43:35', '2024-05-20 23:43:35');
INSERT INTO `user_interface_info` VALUES (224, 2, 15, 'sixkey', '2024-05-21 14:22:20', '2024-05-21 14:22:20');
INSERT INTO `user_interface_info` VALUES (225, 27, 15, 'play', '2024-05-21 18:52:10', '2024-05-21 18:52:10');
INSERT INTO `user_interface_info` VALUES (226, 28, 11, 'test', '2024-05-21 18:57:18', '2024-05-21 18:57:18');
INSERT INTO `user_interface_info` VALUES (227, 29, 13, 'test2', '2024-05-21 18:58:50', '2024-05-21 18:58:50');
INSERT INTO `user_interface_info` VALUES (228, 27, 15, 'play', '2024-05-22 14:45:06', '2024-05-22 14:45:06');
INSERT INTO `user_interface_info` VALUES (229, 1, 15, 'zhangsan', '2024-05-23 15:44:23', '2024-05-23 15:44:23');
INSERT INTO `user_interface_info` VALUES (230, 2, 15, 'zhangsan', '2024-05-27 10:07:16', '2024-05-27 10:07:16');
INSERT INTO `user_interface_info` VALUES (231, 2, 15, 'zhangsan', '2024-05-27 10:07:19', '2024-05-27 10:07:19');
INSERT INTO `user_interface_info` VALUES (232, 27, 15, '23456uhgrthn', '2024-05-31 19:14:53', '2024-05-31 19:14:53');
INSERT INTO `user_interface_info` VALUES (233, 1, 15, 'youke', '2024-06-08 09:42:21', '2024-06-08 09:42:21');
INSERT INTO `user_interface_info` VALUES (234, 1, 15, 'zhangsan', '2024-06-28 10:02:17', '2024-06-28 10:02:17');
INSERT INTO `user_interface_info` VALUES (235, 2, 11, 'zhangsan', '2024-06-28 10:06:55', '2024-06-28 10:06:55');
INSERT INTO `user_interface_info` VALUES (236, 1, 15, 'admin', '2024-08-03 19:23:18', '2024-08-03 19:23:18');
INSERT INTO `user_interface_info` VALUES (237, 1, 15, '23456uhgrthn', '2024-08-03 19:24:19', '2024-08-03 19:24:19');
INSERT INTO `user_interface_info` VALUES (238, 1, 15, 'zhangsan', '2024-08-03 19:25:05', '2024-08-03 19:25:05');
INSERT INTO `user_interface_info` VALUES (239, 1, 15, 'admin', '2024-11-20 19:35:53', '2024-11-20 19:35:53');
INSERT INTO `user_interface_info` VALUES (240, 1, 15, 'admin', '2025-03-21 10:36:39', '2025-03-21 10:36:39');
INSERT INTO `user_interface_info` VALUES (241, 2, 15, 'swx', '2025-03-21 10:38:53', '2025-03-21 10:38:53');
INSERT INTO `user_interface_info` VALUES (242, 2, 14, 'swx', '2025-03-21 10:39:23', '2025-03-21 10:39:23');
INSERT INTO `user_interface_info` VALUES (243, 2, 13, 'swx', '2025-03-21 10:39:53', '2025-03-21 10:39:53');
INSERT INTO `user_interface_info` VALUES (244, 2, 12, 'swx', '2025-03-21 10:40:14', '2025-03-21 10:40:14');
INSERT INTO `user_interface_info` VALUES (245, 1, 11, 'zhangsan', '2025-04-06 13:03:44', '2025-04-06 13:03:44');
INSERT INTO `user_interface_info` VALUES (246, 1, 11, 'zhangsan', '2025-04-06 13:39:53', '2025-04-06 13:39:53');
INSERT INTO `user_interface_info` VALUES (247, 1, 11, 'zhangsan', '2025-04-06 13:42:25', '2025-04-06 13:42:25');
INSERT INTO `user_interface_info` VALUES (248, 1, 11, 'zhangsan', '2025-04-06 13:43:38', '2025-04-06 13:43:38');
INSERT INTO `user_interface_info` VALUES (249, 1, 11, 'zhangsan', '2025-04-06 13:49:13', '2025-04-06 13:49:13');
INSERT INTO `user_interface_info` VALUES (250, 1, 11, 'zhangsan', '2025-04-06 13:51:20', '2025-04-06 13:51:20');
INSERT INTO `user_interface_info` VALUES (251, 1, 15, '23456uhgrthn', '2025-04-06 13:52:11', '2025-04-06 13:52:11');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户角色连表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (20, 1, 1, '2024-04-26 14:48:46', '2024-04-26 14:48:46');
INSERT INTO `user_role` VALUES (25, 8, 8, '2024-05-08 16:45:57', '2024-05-08 16:45:57');
INSERT INTO `user_role` VALUES (26, 8, 10, '2024-05-11 12:29:33', '2024-05-11 12:29:33');
INSERT INTO `user_role` VALUES (27, 8, 11, '2024-05-11 12:29:36', '2024-05-11 12:29:36');
INSERT INTO `user_role` VALUES (28, 8, 12, '2024-05-11 12:29:39', '2024-05-11 12:29:39');
INSERT INTO `user_role` VALUES (29, 8, 13, '2024-05-11 12:42:35', '2024-05-11 12:42:35');
INSERT INTO `user_role` VALUES (30, 8, 15, '2024-05-11 15:19:45', '2024-05-11 15:19:45');
INSERT INTO `user_role` VALUES (31, 8, 16, '2024-05-11 19:59:54', '2024-05-11 19:59:54');
INSERT INTO `user_role` VALUES (32, 8, 17, '2024-05-11 20:01:59', '2024-05-11 20:01:59');
INSERT INTO `user_role` VALUES (33, 8, 18, '2024-05-18 23:06:20', '2024-05-18 23:06:20');
INSERT INTO `user_role` VALUES (34, 8, 19, '2024-05-19 11:01:31', '2024-05-19 11:01:31');
INSERT INTO `user_role` VALUES (35, 8, 20, '2024-05-20 09:34:32', '2024-05-20 09:34:32');
INSERT INTO `user_role` VALUES (36, 8, 21, '2024-05-20 21:17:58', '2024-05-20 21:17:58');
INSERT INTO `user_role` VALUES (37, 8, 22, '2024-05-20 23:38:27', '2024-05-20 23:38:27');
INSERT INTO `user_role` VALUES (52, 8, 27, '2024-05-21 19:55:35', '2024-05-21 19:55:35');
INSERT INTO `user_role` VALUES (53, 14, 27, '2024-05-21 19:55:35', '2024-05-21 19:55:35');
INSERT INTO `user_role` VALUES (54, 8, 28, '2024-05-21 19:55:40', '2024-05-21 19:55:40');
INSERT INTO `user_role` VALUES (55, 14, 28, '2024-05-21 19:55:40', '2024-05-21 19:55:40');
INSERT INTO `user_role` VALUES (58, 8, 29, '2024-05-23 15:42:50', '2024-05-23 15:42:50');
INSERT INTO `user_role` VALUES (59, 14, 29, '2024-05-23 15:42:50', '2024-05-23 15:42:50');
INSERT INTO `user_role` VALUES (66, 8, 2, '2024-06-28 10:06:03', '2024-06-28 10:06:03');

SET FOREIGN_KEY_CHECKS = 1;
