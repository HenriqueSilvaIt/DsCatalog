package com.hen.aula.entities;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_user")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String email;
    private String password; /*vamos criptografar essa senha
    com BCRIPT*/

    @ManyToMany
    @JoinTable(name = "tb_user_role" /*nome da tabela de associação*/,
        joinColumns = @JoinColumn(name = "user_id"), /*
        JoinColumns é o nome da chave
        estrangeira referente a classe onde eu estou*/
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    /*inverseJoinColumns é o nome da chave estrangeira da tabela associada*/
    private Set<Role> roles = new HashSet<>();

    public User() {

    }

    public User(Long id, String firstName, String email, String password) {

        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.password = password;

    }

    public Long getId() {return id;}
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Set<Role> getRoles() {
        return roles;
    }
}
