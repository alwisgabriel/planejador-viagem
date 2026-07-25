package com.planejadorviagem.domain.model;

import java.util.UUID;

public final class User {

    private final UUID id;
    private final String email;
    private final String passwordHash;

    private User(UUID id, String email, String passwordHash) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public static User restore(UUID id, String email, String passwordHash) {
        return new User(id, email, passwordHash);
    }

    public static User create(String email, String passwordHash) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O email não pode estar em branco");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("A senha não pode estar em branco");
        }
        return new User(UUID.randomUUID(), email.trim().toLowerCase(), passwordHash);
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
}
