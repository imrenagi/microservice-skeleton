INSERT INTO roles (id, name) VALUES (1, "ROLE_SUPER_ADMIN");
INSERT INTO roles (id, name) VALUES (2, "ROLE_ADMIN");
INSERT INTO roles (id, name) VALUES (3, "ROLE_USER");
INSERT INTO roles (id, name) VALUES (4, "ROLE_VENDOR");

INSERT INTO permissions (id, name) VALUES (1, "READ_BASIC_INFORMATION");
INSERT INTO permissions (id, name) VALUES (2, "WRITE_BASIC_INFORMATION");
INSERT INTO permissions (id, name) VALUES (3, "CREATE_USER");
INSERT INTO permissions (id, name) VALUES (4, "DELETE_USER");

INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1), (1,2), (1,3), (1,4),
  (2, 1), (2,2), (2,3), (2,4),
  (3, 1), (3,2), (3,3),
  (4,1), (4,2);
