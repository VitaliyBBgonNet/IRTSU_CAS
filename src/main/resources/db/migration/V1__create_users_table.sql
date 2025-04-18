CREATE TABLE users_entity
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(255),
    last_name       VARCHAR(255),
    surname         VARCHAR(255),
    email           VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    position        VARCHAR(255),
    avatar          VARCHAR(255),
    phone           VARCHAR(255),
    department      VARCHAR(255),
    role            VARCHAR(255),
    add_information TEXT,
    created_account TIMESTAMP,
    CONSTRAINT unique_email UNIQUE (email)
);

