package com.jacky.register.models.database.users;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Administer, Integer> {

    Optional<Administer>findByEmail(String email);
}
