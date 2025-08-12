package com.hen.aula.repositories;

import com.hen.aula.entities.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {

    @Query("SELECT obj FROM PasswordRecover obj WHERE obj.token = :token AND obj.expirationToken > :now") /*é um token que ainda
não foi expirado se existir algum token que n expirou ainda vai retornar nessa lista*/
    List<PasswordRecover> searchValidTokens(String token, Instant now);
}

