INSERT INTO users (email, password, first_name, last_name, created_at)
VALUES (
           'admin',
           '$2a$10$SUCxu5vwYbpeMgcW6MdzH.ivsuxLCF2ceWhmc9QBZZfv14w3yGBUC',
           'Admin',
           'DZ',
           CURRENT_TIMESTAMP
       );
INSERT INTO user_roles (user_id, roles) SELECT id, 'ADMIN' FROM users WHERE email = 'admin';

