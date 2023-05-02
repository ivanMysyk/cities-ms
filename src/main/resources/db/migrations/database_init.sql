CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(50) UNIQUE,
    password VARCHAR(120),
    username VARCHAR(20) UNIQUE
);

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(20)
);

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

CREATE TABLE user_roles
(
    user_id SERIAL NOT NULL REFERENCES users (id),
    role_id SERIAL NOT NULL REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE city_photos
(
    id           VARCHAR(36)  NOT NULL,
    name         VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    data         BYTEA        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE cities
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255)  NOT NULL,
    photo_link VARCHAR(1024) NOT NULL,
    photo_id   VARCHAR(36),
    CONSTRAINT fk_photo_id FOREIGN KEY (photo_id) REFERENCES city_photos(id)

);

COPY cities (id, name, photo_link) FROM '/csv/cities.csv' DELIMITER ',' CSV HEADER;