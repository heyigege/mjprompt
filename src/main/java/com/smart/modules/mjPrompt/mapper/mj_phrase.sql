INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522730514165767', 1123598815738675201, 'mj', 'mj_phrase', 'menu', '/mj/mj_phrase', NULL, 1, 1, 0, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522730514165768', '1709522730514165767', 'mj_phrase_add', '新增', 'add', '/mj/mj_phrase/add', 'plus', 1, 2, 1, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522730514165769', '1709522730514165767', 'mj_phrase_edit', '修改', 'edit', '/mj/mj_phrase/edit', 'form', 2, 2, 2, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522730514165770', '1709522730514165767', 'mj_phrase_delete', '删除', 'delete', '/api/mj/mj_phrase/remove', 'delete', 3, 2, 3, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1709522730514165771', '1709522730514165767', 'mj_phrase_view', '查看', 'view', '/mj/mj_phrase/view', 'file-text', 4, 2, 2, 1, NULL, 0);
