package com.hen.aula.repositories;

import com.hen.aula.entities.Product;
import com.hen.aula.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
