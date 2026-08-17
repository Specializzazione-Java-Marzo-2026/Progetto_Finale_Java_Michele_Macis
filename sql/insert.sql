insert into users (username, email, password) values ('admin', 'admin@example.com', '$2a$10$fg6aUnBmdxYKMtLSepvQqO1NIpAtK3I9Ln5GIeRWhgkymb.eiWf8q');

insert into roles (name) values ('ROLE_ADMIN');
insert into roles (name) values ('ROLE_REVISOR');
insert into roles (name) values ('ROLE_WRITER');
insert into roles (name) values ('ROLE_USER');

CREATE TABLE IF NOT EXISTS users_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_users_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_users_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

insert into users_roles (user_id, role_id)
select u.id, r.id
from users u
join roles r on r.name = 'ROLE_ADMIN'
where u.email = 'admin@example.com';