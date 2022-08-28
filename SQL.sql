/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80029 (8.0.29)
 Source Host           : localhost:3306
 Source Schema         : vueadmin

 Target Server Type    : MySQL
 Target Server Version : 80029 (8.0.29)
 File Encoding         : 65001

 Date: 23/08/2022 19:20:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NULL DEFAULT NULL COMMENT 'the first level menu is 0',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'the url of menu',
  `permission_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'the permissions,separated by commas. For example: user:list, user:create',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int NOT NULL COMMENT '0: column, 1: menu, 2: button',
  `icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'icon of menu',
  `sort_num` int NULL DEFAULT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NULL DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, 'System Management', '', 'sys:manage', '', 0, 'el-icon-s-operation', 1, '2021-01-15 18:58:18', '2022-08-15 11:16:27', 1);
INSERT INTO `sys_menu` VALUES (2, 1, 'User', '/sys/users', 'sys:user:list', 'sys/User', 1, 'el-icon-s-custom', 1, '2021-01-15 19:03:45', '2022-08-11 20:37:09', 1);
INSERT INTO `sys_menu` VALUES (3, 1, 'Role', '/sys/roles', 'sys:role:list', 'sys/Role', 1, 'el-icon-rank', 2, '2021-01-15 19:03:45', '2021-01-15 19:03:48', 1);
INSERT INTO `sys_menu` VALUES (4, 1, 'Menu', '/sys/menus', 'sys:menu:list', 'sys/Menu', 1, 'el-icon-menu', 3, '2021-01-15 19:03:45', '2021-01-15 19:03:48', 1);
INSERT INTO `sys_menu` VALUES (5, 0, 'System Tools', '', 'sys:tools', NULL, 0, 'el-icon-s-tools', 2, '2021-01-15 19:06:11', NULL, 1);
INSERT INTO `sys_menu` VALUES (6, 5, 'Digital Dictionary', '/sys/dicts', 'sys:dict:list', 'sys/Dict', 1, 'el-icon-s-order', 1, '2021-01-15 19:07:18', '2021-01-18 16:32:13', 1);
INSERT INTO `sys_menu` VALUES (7, 3, 'Add Role', '', 'sys:role:save', '', 2, '', 1, '2021-01-15 23:02:25', '2022-08-15 12:26:49', 1);
INSERT INTO `sys_menu` VALUES (9, 2, 'Add User', '', 'sys:user:save', NULL, 2, NULL, 1, '2021-01-17 21:48:32', '2022-08-15 12:05:11', 1);
INSERT INTO `sys_menu` VALUES (10, 2, 'Edit User', NULL, 'sys:user:update', NULL, 2, NULL, 2, '2021-01-17 21:49:03', '2021-01-17 21:53:04', 1);
INSERT INTO `sys_menu` VALUES (11, 2, 'Delete Users', NULL, 'sys:user:delete', NULL, 2, NULL, 3, '2021-01-17 21:49:21', NULL, 1);
INSERT INTO `sys_menu` VALUES (12, 2, 'Assign Role', NULL, 'sys:user:role', NULL, 2, NULL, 4, '2021-01-17 21:49:58', NULL, 1);
INSERT INTO `sys_menu` VALUES (13, 2, 'Reset Password', NULL, 'sys:user:repass', NULL, 2, NULL, 5, '2021-01-17 21:50:36', NULL, 1);
INSERT INTO `sys_menu` VALUES (14, 3, 'Edit Role', NULL, 'sys:role:update', NULL, 2, NULL, 2, '2021-01-17 21:51:14', NULL, 1);
INSERT INTO `sys_menu` VALUES (15, 3, 'Delete Roles', NULL, 'sys:role:delete', NULL, 2, NULL, 3, '2021-01-17 21:51:39', NULL, 1);
INSERT INTO `sys_menu` VALUES (16, 3, 'Assign Permissions', NULL, 'sys:role:perm', NULL, 2, NULL, 5, '2021-01-17 21:52:02', NULL, 1);
INSERT INTO `sys_menu` VALUES (17, 4, 'Add Menu', NULL, 'sys:menu:save', NULL, 2, NULL, 1, '2021-01-17 21:53:53', '2021-01-17 21:55:28', 1);
INSERT INTO `sys_menu` VALUES (18, 4, 'Edit Menu', NULL, 'sys:menu:update', NULL, 2, NULL, 2, '2021-01-17 21:56:12', NULL, 1);
INSERT INTO `sys_menu` VALUES (19, 4, 'Delete Menus', NULL, 'sys:menu:delete', NULL, 2, NULL, 3, '2021-01-17 21:56:36', NULL, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `unique_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remark` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime NULL DEFAULT NULL,
  `updated` datetime NULL DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE,
  UNIQUE INDEX `code`(`unique_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (3, 'General User', 'normal', 'Only the basic viewing functions are avaliable', '2021-01-04 10:09:14', '2022-08-18 03:10:08', 1);
INSERT INTO `sys_role` VALUES (6, 'Administrator', 'admin', 'The default highest authority of the system', '2021-01-16 13:29:03', '2022-08-18 03:13:17', 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 134 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (60, 6, 1);
INSERT INTO `sys_role_menu` VALUES (61, 6, 2);
INSERT INTO `sys_role_menu` VALUES (62, 6, 9);
INSERT INTO `sys_role_menu` VALUES (63, 6, 10);
INSERT INTO `sys_role_menu` VALUES (64, 6, 11);
INSERT INTO `sys_role_menu` VALUES (65, 6, 12);
INSERT INTO `sys_role_menu` VALUES (66, 6, 13);
INSERT INTO `sys_role_menu` VALUES (67, 6, 3);
INSERT INTO `sys_role_menu` VALUES (68, 6, 7);
INSERT INTO `sys_role_menu` VALUES (69, 6, 14);
INSERT INTO `sys_role_menu` VALUES (70, 6, 15);
INSERT INTO `sys_role_menu` VALUES (71, 6, 16);
INSERT INTO `sys_role_menu` VALUES (72, 6, 4);
INSERT INTO `sys_role_menu` VALUES (73, 6, 17);
INSERT INTO `sys_role_menu` VALUES (74, 6, 18);
INSERT INTO `sys_role_menu` VALUES (75, 6, 19);
INSERT INTO `sys_role_menu` VALUES (76, 6, 5);
INSERT INTO `sys_role_menu` VALUES (77, 6, 6);
INSERT INTO `sys_role_menu` VALUES (128, 3, 1);
INSERT INTO `sys_role_menu` VALUES (129, 3, 2);
INSERT INTO `sys_role_menu` VALUES (130, 3, 3);
INSERT INTO `sys_role_menu` VALUES (131, 3, 4);
INSERT INTO `sys_role_menu` VALUES (132, 3, 5);
INSERT INTO `sys_role_menu` VALUES (133, 3, 6);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime NULL DEFAULT NULL,
  `updated` datetime NULL DEFAULT NULL,
  `last_login` datetime NULL DEFAULT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_USERNAME`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'Racheal', '$2a$10$m8QLgo1x7UAHGb2/tpNkkegMAOlHHcMY.9UJnoAoDB1hHLegkq8AG', 'https://cdn-icons.flaticon.com/png/512/2171/premium/2171990.png?token=exp=1660791504~hmac=697638dd5580d6d332b571cf588e01f6', '123@qq.com', '广州', '2021-01-12 22:13:53', '2022-08-18 03:33:22', '2020-12-30 08:38:37', 1);
INSERT INTO `sys_user` VALUES (2, 'admin', '$2a$10$xJBw6SgJ4YB4yE2m6tsetOHkvE7C2rUXSHUZN7PfxjJCKoZBsi.Ze', 'https://cdn-icons.flaticon.com/png/512/1960/premium/1960008.png?token=exp=1660791409~hmac=cc4329d9972a7ef40be6639f75e03ee8', 'test@qq.com', NULL, '2021-01-30 08:20:22', '2022-08-18 10:49:03', NULL, 1);
INSERT INTO `sys_user` VALUES (3, 'Eleen', '$2a$10$077mCsXtZGAYyg1CMKFTNebJAP0UWpKVx17DUUK8VOo9NTBtlx1Ue', 'https://cdn-icons.flaticon.com/png/512/2423/premium/2423917.png?token=exp=1660791594~hmac=94752aac1ffb4e6d33cba3c3d6188a4d', '123@qq.com', 'Guangzhou', '2021-01-12 22:13:53', '2022-08-18 10:48:54', '2020-12-30 08:38:37', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (13, 3, 3);
INSERT INTO `sys_user_role` VALUES (19, 1, 3);
INSERT INTO `sys_user_role` VALUES (20, 2, 3);
INSERT INTO `sys_user_role` VALUES (21, 2, 6);

SET FOREIGN_KEY_CHECKS = 1;
