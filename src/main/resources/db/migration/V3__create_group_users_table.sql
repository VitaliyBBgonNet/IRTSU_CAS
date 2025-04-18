CREATE TABLE group_users
(
    group_id UUID NOT NULL,
    user_id  UUID NOT NULL,
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users_entity (id) ON DELETE CASCADE
);