package com.hen.aula.resources;

import com.hen.aula.dto.*;
import com.hen.aula.services.AuthService;
import com.hen.aula.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController /*Transforma a classe em um recurso Rest, esse anotation
 efetua um pré processamento ao compila essa clase*/
@RequestMapping(value = "/auth") /*Aqui você passa a rota*/
public class AuthResource {



    @Autowired
    private AuthService authService;


    /*Response Entity é um objeto do spring que vai encapsular o resultado da
    requisição
     */@PostMapping(value = "/recover-token")
    public ResponseEntity <Void> createRecoverToken(@Valid @RequestBody EmailDTO body) {

         authService.createRecoverToken(body);
        return ResponseEntity.noContent().build();
        /* .body  é para definir o corpo da resposta você pode colocar
        * o list dentro do ok, porque ele tem uma sobrecarga dentro do ok
        * que aceita o corpo*/

    }
    /*Response Entity é um objeto do spring que vai encapsular o resultado da
requisição
 */@PutMapping(value = "/new-password")
    public ResponseEntity <Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO body) {

        authService.saveNewPassword(body);
        return ResponseEntity.noContent().build();
        /* .body  é para definir o corpo da resposta você pode colocar
         * o list dentro do ok, porque ele tem uma sobrecarga dentro do ok
         * que aceita o corpo*/

    }


}