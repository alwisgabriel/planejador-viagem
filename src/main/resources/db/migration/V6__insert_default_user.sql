INSERT INTO users (id, email, password_hash, created_at)
VALUES ('00000000-0000-0000-0000-000000000001', 'default@local', '$2a$10$dummy', NOW())
ON CONFLICT (id) DO NOTHING;
