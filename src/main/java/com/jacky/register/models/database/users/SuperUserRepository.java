package com.jacky.register.models.database.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuperUserRepository extends JpaRepository<SuperAdmin, Integer> {

    public Optional<SuperAdmin> findByEmail(String email);
}
