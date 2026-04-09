INSERT INTO categories(name, description, ticket_type_id)
VALUES ('Hardware', 'Hardware category for incident.', 1),
       ('Software', 'Software category for incident.', 1),
       ('Hardware', 'Hardware category for request.', 2),
       ('Software', 'Software category for request.', 2),
       ('Hardware', 'Hardware category for problem.', 3),
       ('Software', 'Software category for problem.', 3) ON CONFLICT (name, ticket_type_id) DO NOTHING;