INSERT INTO ticket_types (id, name, description)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'incident', 'Incident type.'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'request', 'Request type.'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'problem', 'Problem type.')
    ON CONFLICT (name) DO NOTHING;