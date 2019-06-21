DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (dateTime, description, calories, userId) VALUES
  ('2019-06-20 12:00:00-00', 'Завтрак', 500, 100000),
  ('2019-06-20 14:00:00-00', 'Обед', 500, 100000),
  ('2019-06-20 18:00:00-00', 'Ужин', 700, 100000),
  ('2019-06-21 12:00:00-00', 'Завтрак', 500, 100000),
  ('2019-06-21 14:00:00-00', 'Обед', 500, 100000),
  ('2019-06-21 18:00:00-00', 'Ужин', 700, 100000),
  ('2019-06-21 12:00:00-00', 'Завтрак', 500, 100001),
  ('2019-06-21 14:00:00-00', 'Обед', 500, 100001),
  ('2019-06-21 18:00:00-00', 'Ужин', 400, 100001);