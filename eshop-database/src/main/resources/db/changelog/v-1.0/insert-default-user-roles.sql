insert into users (id, age, password, username)
values (1, 32, '$2a$10$uedJ6jkBS08x5mxZY6gV6.LAKSd202CiVutxz5VDq3TIyj9alkmIq', 'admin');

insert into users (id, age, password, username)
values (2, 28, '$2a$10$uedJ6jkBS08x5mxZY6gV6.LAKSd202CiVutxz5VDq3TIyj9alkmIq', 'guest');

insert into roles (id, name)
values (1, 'ROLE_ADMIN');

insert into roles (id, name)
values (2, 'ROLE_GUEST');

INSERT INTO users_roles
SELECT (SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
UNION ALL
SELECT (SELECT id FROM users WHERE username = 'guest'), (SELECT id FROM roles WHERE name = 'ROLE_GUEST');