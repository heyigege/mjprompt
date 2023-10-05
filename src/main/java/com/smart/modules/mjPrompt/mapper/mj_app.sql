INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522727364243462', 0, 'mj', 'mj_app', 'menu', '/mj/mj_app', NULL, 1, 1, 0, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522727364243463', '1709522727364243462', 'mj_app_add', '新增', 'add', '/mj/mj_app/add', 'plus', 1, 2, 1, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522727364243464', '1709522727364243462', 'mj_app_edit', '修改', 'edit', '/mj/mj_app/edit', 'form', 2, 2, 2, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522727364243465', '1709522727364243462', 'mj_app_delete', '删除', 'delete', '/api/mj/mj_app/remove', 'delete', 3, 2, 3, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522727364243466', '1709522727364243462', 'mj_app_view', '查看', 'view', '/mj/mj_app/view', 'file-text', 4, 2, 2, 1, NULL, 0);
