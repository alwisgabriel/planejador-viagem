CREATE TABLE destinations (
    id UUID PRIMARY KEY,
    trip_id UUID NOT NULL,
    city VARCHAR(120) NOT NULL,
    country VARCHAR(120) NOT NULL,
    display_order INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_destinations_trip
        FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE,
    CONSTRAINT ck_destinations_display_order
        CHECK (display_order > 0),
    CONSTRAINT uq_destinations_trip_order
        UNIQUE (trip_id, display_order)
);

CREATE INDEX idx_destinations_trip_id ON destinations (trip_id);
