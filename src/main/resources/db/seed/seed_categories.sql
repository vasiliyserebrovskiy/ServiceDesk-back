INSERT INTO categories (id, name, description, ticket_type_id)
VALUES
    ('d1111111-1111-1111-1111-111111111111', 'Hardware', 'Hardware category for incident.', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('d2222222-2222-2222-2222-222222222222', 'Software', 'Software category for incident.', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),

    ('d3333333-3333-3333-3333-333333333333', 'Hardware', 'Hardware category for request.', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
    ('d4444444-4444-4444-4444-444444444444', 'Software', 'Software category for request.', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),

    ('d5555555-5555-5555-5555-555555555555', 'Hardware', 'Hardware category for problem.', 'cccccccc-cccc-cccc-cccc-cccccccccccc'),
    ('d6666666-6666-6666-6666-666666666666', 'Software', 'Software category for problem.', 'cccccccc-cccc-cccc-cccc-cccccccccccc')
    ON CONFLICT (name, ticket_type_id) DO NOTHING;