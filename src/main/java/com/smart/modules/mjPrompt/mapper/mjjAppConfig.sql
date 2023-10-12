INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1712133184936550407', 0, 'mj', 'mjjAppConfig', 'menu', '/mj/mjjAppConfig', NULL, 1, 1, 0, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1712133184936550408', '1712133184936550407', 'mjjAppConfig_add', '新增', 'add', '/mj/mjjAppConfig/add', 'plus', 1, 2, 1, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1712133184936550409', '1712133184936550407', 'mjjAppConfig_edit', '修改', 'edit', '/mj/mjjAppConfig/edit', 'form', 2, 2, 2, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1712133184936550410', '1712133184936550407', 'mjjAppConfig_delete', '删除', 'delete', '/api/mj/mjjAppConfig/remove', 'delete', 3, 2, 3, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1712133184936550411', '1712133184936550407', 'mjjAppConfig_view', '查看', 'view', '/mj/mjjAppConfig/view', 'file-text', 4, 2, 2, 1, NULL, 0);
