package com.planejadorviagem.adapter.out.persistence;

import com.planejadorviagem.adapter.out.persistence.entity.UserEntity;
import com.planejadorviagem.adapter.out.persistence.repository.UserJpaRepository;
import com.planejadorviagem.application.port.out.UserRepository;
import com.planejadorviagem.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public final class UserJpaAdapter implements UserRepository {

    private final UserJpaRepository repository;

    public UserJpaAdapter(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(user.getId(), user.getEmail(), user.getPasswordHash());
        repository.save(entity);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id).map(e -> User.restore(e.getId(), e.getEmail(), e.getPasswordHash()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(e -> User.restore(e.getId(), e.getEmail(), e.getPasswordHash()));
    }
}
