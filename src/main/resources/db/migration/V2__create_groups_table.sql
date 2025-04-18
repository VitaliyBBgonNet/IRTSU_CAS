CREATE TABLE groups
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    logo        VARCHAR(255),
    admin_id    UUID         NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES users_entity (id) ON DELETE RESTRICT
);