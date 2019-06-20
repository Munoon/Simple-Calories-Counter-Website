DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (description, calories, user_id) VALUES
  ('Завтрак', 500, 100000),
  ('Обед', 500, 100000),
  ('Ужин', 700, 100000),
  ('Завтрак', 500, 100001),
  ('Обед', 500, 100001),
  ('Ужин', 400, 100001);