-- =============================================
-- 消息管理菜单及权限 - SQL脚本
-- =============================================

-- 1. 检查并删除已存在的消息管理相关菜单
DELETE FROM sys_menu WHERE menu_name IN ('消息管理', '消息查询', '发送消息', '广播消息') AND component LIKE
                                                                                             '%message%';

-- 2. 添加消息管理主菜单
INSERT INTO sys_menu (
    menu_name, parent_id, order_num, path, component,
    is_frame, is_cache, menu_type, visible, status,
    perms, icon, create_time, update_time, remark
) VALUES (
             '消息管理', 0, 1000, '/system/message', 'system/message/SysMessage',
             1, 0, 'C', '0', '0',
             'system:message:list', 'mail', NOW(), NOW(), '系统消息管理菜单'
         );

-- 3. 获取刚插入的菜单ID（消息管理）
SET @message_menu_id = LAST_INSERT_ID();

  -- 4. 添加消息管理按钮权限
INSERT INTO sys_menu (
    menu_name, parent_id, order_num, perms, menu_type, visible, status,
    create_time, update_time, remark
) VALUES
      -- 发送消息按钮
      ('发送消息', @message_menu_id, 1, 'system:message:send', 'F', '0', '0', NOW(), NOW(), '发送消息权限'),
      -- 广播消息按钮
      ('广播消息', @message_menu_id, 2, 'system:message:broadcast', 'F', '0', '0', NOW(), NOW(), '广播消息权限'),
      -- 标记已读按钮
      ('标记已读', @message_menu_id, 3, 'system:message:read', 'F', '0', '0', NOW(), NOW(), '标记已读权限'),
      -- 删除消息按钮
      ('删除消息', @message_menu_id, 4, 'system:message:delete', 'F', '0', '0', NOW(), NOW(), '删除消息权限');

-- 5. 为管理员角色添加消息管理权限（假设admin角色的role_id为1，请根据实际情况修改）
-- 先删除可能已存在的旧权限
DELETE FROM sys_role_menu WHERE role_id = 1 AND menu_id IN (
    SELECT menu_id FROM sys_menu WHERE component = 'system/message/SysMessage'
    UNION
    SELECT menu_id FROM sys_menu WHERE perms LIKE 'system:message:%'
);

-- 6. 为管理员角色添加所有消息管理权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu
WHERE (component = 'system/message/SysMessage' OR perms LIKE 'system:message:%');

-- 7. 查询验证
SELECT
    m.menu_id,
    m.menu_name,
    m.parent_id,
    m.order_num,
    m.path,
    m.component,
    m.perms,
    m.icon,
    m.menu_type,
    rm.role_id
FROM sys_menu m
         LEFT JOIN sys_role_menu rm ON m.menu_id = rm.menu_id AND rm.role_id = 1
WHERE m.component = 'system/message/SysMessage' OR m.perms LIKE 'system:message:%'
ORDER BY m.order_num;

-- =============================================
-- SQL脚本执行完成
-- =============================================