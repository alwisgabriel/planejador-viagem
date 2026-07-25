CREATE TABLE trips (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    budget NUMERIC(12, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_trips_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT ck_trips_dates
        CHECK (end_date >= start_date),
    CONSTRAINT ck_trips_budget
        CHECK (budget >= 0)
);

CREATE INDEX idx_trips_user_id ON trips (user_id);
