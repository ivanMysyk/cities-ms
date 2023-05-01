package com.test.task.kuehne.cities.repository;

import com.test.task.kuehne.cities.model.ERole;
import com.test.task.kuehne.cities.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
