use myths;

insert into authorities(id, name)
values
    (1, "READ"),
    (2, "WRITE"),
    (3, "UPDATE"),
    (4, "DELETE");

insert into roles(id, name)
values
    (1, "ROLE_USER"),
    (2, "ROLE_STAFF"),
    (3, "ROLE_ADMIN");

insert into roles_authorities(role_id, authority_id)
values
    (1, 1),
    (2, 1),	(2, 2),	(2, 3),
    (3, 1),	(3, 2),	(3, 3),	(3, 4);

insert into users(
    first_name, last_name,
    email, username,
    password,
    join_date, role_id,
    is_active, is_not_locked
)
values(
      "Kaloyan", "Ivanov",
      "provinsky0@gmail.com", "kaliv0",
      "$2a$10$J3GrwSAbh/Y9aFt58E7Pk.DMV/lwUwrKk8U6PeWhw7vDe0odxsV0y",
      utc_timestamp(), 3,
      1, 1
);