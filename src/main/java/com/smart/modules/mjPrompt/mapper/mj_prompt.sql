INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1711030949502857223', 0, 'mj', 'mj_prompt', 'menu', '/mj/mj_prompt', NULL, 1, 1, 0, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1711030949502857224', '1711030949502857223', 'mj_prompt_add', '新增', 'add', '/mj/mj_prompt/add', 'plus', 1, 2, 1, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1711030949502857225', '1711030949502857223', 'mj_prompt_edit', '修改', 'edit', '/mj/mj_prompt/edit', 'form', 2, 2, 2, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1711030949502857226', '1711030949502857223', 'mj_prompt_delete', '删除', 'delete', '/api/mj/mj_prompt/remove', 'delete', 3, 2, 3, 1, NULL, 0);
INSERT INTO `smart_menu`(`id`, `parent_id`, `code`, `name`, `alias`, `path`, `source`, `sort`, `category`, `action`, `is_open`, `remark`, `is_deleted`)
VALUES ('1711030949502857227', '1711030949502857223', 'mj_prompt_view', '查看', 'view', '/mj/mj_prompt/view', 'file-text', 4, 2, 2, 1, NULL, 0);
