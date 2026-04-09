INSERT INTO ticket_types(name, description)
VALUES ('incident', 'Incident type.'),
       ('request', 'Request type.'),
       ('problem', 'Problem type.') ON CONFLICT (name) DO NOTHING;