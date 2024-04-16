INSERT INTO
    application_user (email, password)
VALUES
--     superadmin@example.com / password
    ('superadmin@example.com', '{bcrypt}$2a$10$n8rXtCstodhvIwQgBkrUl.YJqI9m0S.6vvrsUYBbauvt0D/QcqdEu'),
--     tom@example.com / native.5
    ('tom@example.com', '{bcrypt}$2a$10$m5Ky/7xHuAf82Mc9pYSy2uBbrLD37FhaNqUzUfLKuCOBdVAsE3MbC'),
--     john@example.com / notHard
    ('john@example.com', '{bcrypt}$2a$10$ShVIEtyX1xe0fpjMI19J3O8OIL9EYU34r0u4pxzc5c5Yv1nRtPmOG');

INSERT INTO
    user_role (name, description)
VALUES
    ('ADMIN', 'Has access to everything'),
    ('USER', 'Has standard access');

INSERT INTO
    user_roles (user_id, role_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 2);
