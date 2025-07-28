package com.hen.aula.repositories;

import com.hen.aula.entities.Role;
import com.hen.aula.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
