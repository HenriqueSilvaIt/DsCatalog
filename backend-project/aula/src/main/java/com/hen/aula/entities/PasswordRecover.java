package com.hen.aula.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tb_passoword_recover")
public class PasswordRecover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) /*tornando o campo obrigátorio*/
    private String token; /*token para o usuário conseguir
    receber o link de recuperação de senha por email
    */

    @Column(nullable = false) /*tornando o campo obrigátorio*/
    private String email; /*email para identificar o usuário*/

    @Column(nullable = false) /*tornando o campo obrigátorio*/
    private Instant expirationToken; /*tempo de expiração do token */

    public PasswordRecover() {

    }

    public PasswordRecover(Long id, Instant expirationToken, String email, String token) {
        this.id = id;
        this.expirationToken = expirationToken;
        this.email = email;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getExpirationToken() {
        return expirationToken;
    }

    public void setExpirationToken(Instant expirationToken) {
        this.expirationToken = expirationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PasswordRecover that = (PasswordRecover) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
