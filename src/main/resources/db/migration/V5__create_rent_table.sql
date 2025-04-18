CREATE TABLE rent
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID NOT NULL,
    detail_id     UUID NOT NULL,
    start_date    TIMESTAMP,
    end_date      TIMESTAMP,
    rental_status VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users_entity (id) ON DELETE RESTRICT,
    FOREIGN KEY (detail_id) REFERENCES details_entity (id) ON DELETE CASCADE
);