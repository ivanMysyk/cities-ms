CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(50) UNIQUE,
                       password VARCHAR(120),
                       username VARCHAR(20) UNIQUE
);

CREATE TABLE roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(20)
);

CREATE TABLE user_roles (
                            user_id SERIAL NOT NULL REFERENCES users(id),
                            role_id SERIAL NOT NULL REFERENCES roles(id),
                            PRIMARY KEY(user_id, role_id)
);