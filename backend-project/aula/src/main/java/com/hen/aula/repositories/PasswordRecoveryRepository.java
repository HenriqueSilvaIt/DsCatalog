package com.hen.aula.repositories;

import com.hen.aula.entities.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecover, Long> {
}
