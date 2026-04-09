INSERT INTO roles(name, description, default_role)
VALUES ('USER', 'Default user role.', true),
       ('MANAGER', 'Manager user role.', false),
       ('ADMIN', 'Admin user role.', false) ON CONFLICT (name) DO NOTHING;