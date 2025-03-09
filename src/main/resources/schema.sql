CREATE TABLE IF NOT EXISTS productos
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    nombre
    VARCHAR
(
    255
) NOT NULL,
    precio DOUBLE NOT NULL
    );/**/


CREATE TABLE IF NOT EXISTS usuarios
(
    id
    SERIAL
    PRIMARY
    KEY,
    username
    VARCHAR
(
    50
) UNIQUE NOT NULL,
    password VARCHAR
(
    255
) NOT NULL,
    roles VARCHAR
(
    255
) NOT NULL,
    enabled BOOLEAN NOT NULL,
    account_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL
    );