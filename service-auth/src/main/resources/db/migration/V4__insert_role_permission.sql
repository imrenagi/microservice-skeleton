INSERT INTO roles (id, name) VALUES (1, "ROLE_SUPER_ADMIN");
INSERT INTO roles (id, name) VALUES (2, "ROLE_ADMIN");
INSERT INTO roles (id, name) VALUES (3, "ROLE_USER");
INSERT INTO roles (id, name) VALUES (4, "ROLE_VENDOR");

INSERT INTO permissions (id, name) VALUES (1, "READ_TIMELINE_POST");
INSERT INTO permissions (id, name) VALUES (2, "WRITE_TIMELINE_POST");
INSERT INTO permissions (id, name) VALUES (3, "CREATE_NEW_USER");
INSERT INTO permissions (id, name) VALUES (4, "DELETE_ACCOUNT");

INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1), (1,2), (1,3), (1,4),
  (2, 1), (2,2), (2,3), (2,4),
  (3, 1), (3,3),
  (4,1), (4,2), (4,3), (4,4);
