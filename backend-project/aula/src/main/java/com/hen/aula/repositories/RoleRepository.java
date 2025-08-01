package com.hen.aula.repositories;

import com.hen.aula.entities.Role;
import com.hen.aula.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    /*Busca do banco o  perfil que tem o nome de perfil que eu passar*/
    Role findByAuthority(String authority);
            /*Para funcionar o QueryMethods tem que ficar
            * certinho o nome do atributo autority dentro da entidade
            * só com a primeira letra maiúscula */
}
