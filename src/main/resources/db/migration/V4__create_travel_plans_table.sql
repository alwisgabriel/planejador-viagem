CREATE TABLE travel_plans (
    id UUID PRIMARY KEY,
    trip_id UUID NOT NULL,
    content JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_travel_plans_trip
        FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE
);

CREATE INDEX idx_travel_plans_trip_id ON travel_plans (trip_id);
