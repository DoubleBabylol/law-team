-- ==================== 机构数据 ====================
-- 总公司 (level 1)
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('100000', '总公司', NULL, 1);

-- 省级分公司 (level 2)
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('110000', '北京市分公司', '100000', 2);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('310000', '上海市分公司', '100000', 2);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('440000', '广东省分公司', '100000', 2);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('330000', '浙江省分公司', '100000', 2);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('320000', '江苏省分公司', '100000', 2);

-- 地市级支公司 (level 3) - 北京下
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('110100', '北京朝阳支公司', '110000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('110200', '北京海淀支公司', '110000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('110300', '北京丰台支公司', '110000', 3);

-- 地市级支公司 (level 3) - 上海下
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('310100', '上海浦东支公司', '310000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('310200', '上海黄浦支公司', '310000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('310300', '上海徐汇支公司', '310000', 3);

-- 地市级支公司 (level 3) - 广东下
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('440100', '广州支公司', '440000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('440200', '深圳支公司', '440000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('440300', '东莞支公司', '440000', 3);

-- 地市级支公司 (level 3) - 浙江下
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('330100', '杭州支公司', '330000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('330200', '宁波支公司', '330000', 3);

-- 地市级支公司 (level 3) - 江苏下
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('320100', '南京支公司', '320000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('320200', '苏州支公司', '320000', 3);
INSERT OR IGNORE INTO sys_branch (branch_no, branch_name, prio_branch_no, level) VALUES ('320300', '无锡支公司', '320000', 3);

-- ==================== 角色数据 ====================
INSERT OR IGNORE INTO sys_role (role_id, role_name, role_group, status, create_time, update_time)
VALUES ('role001', '总经办', '合规', 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_role (role_id, role_name, role_group, status, create_time, update_time)
VALUES ('role002', '总审核', '合规', 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_role (role_id, role_name, role_group, status, create_time, update_time)
VALUES ('role003', '省经办', '督察', 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_role (role_id, role_name, role_group, status, create_time, update_time)
VALUES ('role004', '省审核', '督察', 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_role (role_id, role_name, role_group, status, create_time, update_time)
VALUES ('role005', '市经办', '诉讼', 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_role (role_id, role_name, role_group, status, create_time, update_time)
VALUES ('role006', '市审核', '诉讼', 1, datetime('now'), NULL);

-- ==================== 菜单数据 ====================
-- 一级目录: 工作台
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu001', NULL, '工作台', '目录', '/dashboard', NULL, 'Monitor', 1, 1, datetime('now'), NULL);

-- 一级目录: 系统管理
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu002', NULL, '系统管理', '目录', '/system', NULL, 'Setting', 2, 1, datetime('now'), NULL);

-- 二级菜单: 角色管理
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu003', 'menu002', '角色管理', '菜单', '/system/role', NULL, 'UserFilled', 1, 1, datetime('now'), NULL);

-- 角色管理下的按钮
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu004', 'menu003', '新增角色', '按钮', NULL, 'system:role:add', NULL, 1, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu005', 'menu003', '编辑角色', '按钮', NULL, 'system:role:edit', NULL, 2, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu006', 'menu003', '删除角色', '按钮', NULL, 'system:role:delete', NULL, 3, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu007', 'menu003', '查询角色', '按钮', NULL, 'system:role:query', NULL, 4, 1, datetime('now'), NULL);

-- 二级菜单: 用户管理
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu008', 'menu002', '用户管理', '菜单', '/system/user', NULL, 'User', 2, 1, datetime('now'), NULL);

-- 用户管理下的按钮
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu009', 'menu008', '新增用户', '按钮', NULL, 'system:user:add', NULL, 1, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu010', 'menu008', '编辑用户', '按钮', NULL, 'system:user:edit', NULL, 2, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu011', 'menu008', '删除用户', '按钮', NULL, 'system:user:delete', NULL, 3, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu012', 'menu008', '重置密码', '按钮', NULL, 'system:user:resetPwd', NULL, 4, 1, datetime('now'), NULL);

-- 二级菜单: 菜单管理
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu013', 'menu002', '菜单管理', '菜单', '/system/menu', NULL, 'Menu', 3, 1, datetime('now'), NULL);

-- 菜单管理下的按钮
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu014', 'menu013', '新增菜单', '按钮', NULL, 'system:menu:add', NULL, 1, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu015', 'menu013', '编辑菜单', '按钮', NULL, 'system:menu:edit', NULL, 2, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu016', 'menu013', '删除菜单', '按钮', NULL, 'system:menu:delete', NULL, 3, 1, datetime('now'), NULL);

-- 一级目录: 权限配置
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu017', NULL, '权限配置', '目录', '/permission', NULL, 'Lock', 3, 1, datetime('now'), NULL);

-- 二级菜单: 角色权限
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu018', 'menu017', '角色权限', '菜单', '/permission/role', NULL, 'Key', 1, 1, datetime('now'), NULL);

-- 角色权限下的按钮
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu019', 'menu018', '配置权限', '按钮', NULL, 'permission:role:config', NULL, 1, 1, datetime('now'), NULL);

-- ==================== 管理员账号 ====================
-- 密码 123456 的 BCrypt 加密值
INSERT OR IGNORE INTO sys_user (user_id, username, clerk_no, branch_no, branch_name, dept_name, dept_no, password, status, create_time, update_time)
VALUES ('admin001', '张三', 'GH001', '100000', '总公司', '合规部', 'D001',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 1, datetime('now'), NULL);

-- ==================== 用户角色关联 ====================
-- 给张三分配"总经办"角色
INSERT OR IGNORE INTO sys_user_role (user_id, role_id, create_time)
VALUES ('admin001', 'role001', datetime('now'));

-- ==================== 角色菜单关联（给总经办分配所有菜单权限）====================
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu001', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu002', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu003', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu004', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu005', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu006', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu007', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu008', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu009', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu010', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu011', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu012', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu013', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu014', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu015', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu016', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu017', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu018', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu019', datetime('now'));

-- ==================== 诉讼管理菜单 ====================
-- 一级目录: 诉讼管理
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu020', NULL, '诉讼管理', '目录', '/lawsuit', NULL, 'Briefcase', 4, 1, datetime('now'), NULL);

-- 二级菜单: 重大索赔事项
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu021', 'menu020', '重大索赔事项', '菜单', '/lawsuit/claims', NULL, 'Document', 1, 1, datetime('now'), NULL);

-- 重大索赔事项下的按钮
INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu022', 'menu021', '新增索赔', '按钮', NULL, 'lawsuit:claim:add', NULL, 1, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu023', 'menu021', '编辑索赔', '按钮', NULL, 'lawsuit:claim:edit', NULL, 2, 1, datetime('now'), NULL);

INSERT OR IGNORE INTO sys_menu (resource_id, parent_resource_id, resource_name, resource_type, resource_path, permission, icon, sort_order, status, create_time, update_time)
VALUES ('menu024', 'menu021', '审批索赔', '按钮', NULL, 'lawsuit:claim:approve', NULL, 3, 1, datetime('now'), NULL);

-- 给总经办角色分配诉讼管理菜单权限
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu020', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu021', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu022', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu023', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role001', 'menu024', datetime('now'));

-- 给总审核角色分配诉讼管理菜单权限
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role002', 'menu020', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role002', 'menu021', datetime('now'));
INSERT OR IGNORE INTO sys_role_menu (role_id, resource_id, create_time) VALUES ('role002', 'menu024', datetime('now'));
