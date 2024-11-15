-- 插入初始数据
INSERT INTO user (username, password, phone_number, role, create_time, update_time, is_delete)
VALUES ('admin', MD5('admin'), '12345678910', 'ADMIN', NOW(), NOW(), 0),
       ('user', MD5('user'), '10987654321', 'USER', NOW(), NOW(), 0);
