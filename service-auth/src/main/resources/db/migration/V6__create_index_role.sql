CREATE INDEX role_permission_index ON role_permissions (role_id, permission_id);
CREATE INDEX user_role_index ON user_roles (user_id, role_id);

CREATE INDEX role_name_index ON roles (name);
CREATE INDEX permission_name_index ON permissions (name);