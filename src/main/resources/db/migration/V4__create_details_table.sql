CREATE TABLE details
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    image             VARCHAR(255),
    name              VARCHAR(255) NOT NULL,
    description       TEXT,
    documentation     TEXT,
    group_id          UUID,
    owner_id          UUID         NOT NULL,
    status            VARCHAR(255),
    created_detail    TIMESTAMP,
    tenant_id         UUID,
    moderation_status VARCHAR(255),
    FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE SET NULL,
    FOREIGN KEY (owner_id) REFERENCES users_entity (id) ON DELETE RESTRICT,
    FOREIGN KEY (tenant_id) REFERENCES users_entity (id) ON DELETE SET NULL
);