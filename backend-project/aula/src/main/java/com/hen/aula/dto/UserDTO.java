package com.hen.aula.dto;

import com.hen.aula.entities.Role;
import com.hen.aula.entities.User;
import com.hen.aula.services.validation.UserInsertValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {

    private Long id;


    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;
    @Email(message = "Favor entrar um e-mail válido")
    private String email;
    /*não é recomendado deixa senha no dto
    * mesmo criptografad*/

    /*RoleDTO para colocar no JSON as permissões do usuário*/
    Set<RoleDTO> roles = new HashSet <>();

    public UserDTO () {

    }
    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserDTO(User entity) {
        /*não precisa do this*/
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        /*eu acesso a lista de roles que está dentro do user
        * percorro cada um dos perfis que tiver dentro do usuário
        * eu vou instanciar um ROLEDTO a partir dele e inserir
        * esses roles da entity na listinha Set<Role> roles que está
        * nesse DTO*/
        entity.getRoles().forEach(x -> this.roles.add(new RoleDTO(x)));

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
