package com.hen.aula.resources;

import com.hen.aula.dto.UserDTO;
import com.hen.aula.dto.UserInsertDTO;
import com.hen.aula.dto.UserUpdateDTO;
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
@RequestMapping(value = "/users") /*Aqui você passa a rota*/
public class UserResource {


    @Autowired
    private UserService service;


    /*Response Entity é um objeto do spring que vai encapsular o resultado da
    requisição
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity <Page<UserDTO>> findAll(Pageable pageable) {


        Page<UserDTO> list = service.findAllPaged(pageable);

        return ResponseEntity.ok().body(list);
        /* .body  é para definir o corpo da resposta você pode colocar
        * o list dentro do ok, porque ele tem uma sobrecarga dentro do ok
        * que aceita o corpo*/

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    /*path variable é o parâmetro que você vai passa na rota*/
    public ResponseEntity<UserDTO> findByid(@PathVariable Long id) {
        UserDTO dto  = service.findById(id);

        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_OPERATOR','ROLE_ADMIN')")
    @GetMapping(value = "/authenticated")
    /*path variable é o parâmetro que você vai passa na rota*/
    public ResponseEntity<UserDTO> findUserAuthenticated() {
        UserDTO dto  = service.findUserAuthenticated();

        return ResponseEntity.ok().body(dto);
    }



    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {

        UserDTO newDto = service.insert(dto); /*como o
        service apesar de receber como argunto UserInsertDTO
        ele retorna um UserDTO, etão nós instanciamos
        um UserDTO recebendo as informações do UserInserDTO que além de
        ter todas informações do usuário  também
         tem  senha criptografada*/

        /*Objeto que pega o endereço do objeto criado*/
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id")
                .buildAndExpand(newDto.getId()).toUri();

        return ResponseEntity.created(uri).body(newDto);
        /*create retorna 201 que é o código mais correto de recurso criado
        convém você também retornar o Location que é o endereço
         desse novo recurso criado na aba header do postman na requisição que mostrar o local
        de onde 201 http location header*/

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")/*Put é um método não idepotente,
    salvar recurso de forma idempotente (se existir o produto do mesmo id, ele salva  o mesmo produto e não salva outro)*/
    public ResponseEntity<UserDTO> update (
            @PathVariable Long id,
            @Valid
            @RequestBody UserUpdateDTO dto) {

        UserDTO newDto = service.update(id, dto);/*como o
        service apesar de receber como argunto UserUpdatetDTO
        ele retorna um UserDTO, etão nós instanciamos
        um UserDTO recebendo as informações do UserUpdatetDTO que além de
        ter todas informações do usuário  também
         tem  senha criptografada*/

        return ResponseEntity.ok().body(newDto);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping(value =  "/{id}") /*No padrão REST utilizamos o delete do HTTP*/
    public ResponseEntity<Void> delete (@PathVariable Long id) {/*Delete
    não tem corpo na resposta por isso podemos usar void */
        service.delete(id);
        return ResponseEntity.noContent().build();
        /*204 quer dizer que deu certo mas no corpo da resposta está vaizo*/
    }
}