INSERT INTO roles (id, name, description, default_role)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'USER', 'Default user role.', true),
    ('22222222-2222-2222-2222-222222222222', 'MANAGER', 'Manager user role.', false),
    ('33333333-3333-3333-3333-333333333333', 'ADMIN', 'Admin user role.', false)
    ON CONFLICT (name) DO NOTHING;