package com.hen.aula.resources;

import com.hen.aula.CategoryDTO;
import com.hen.aula.entities.Category;
import com.hen.aula.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController /*Transforma a classe em um recurso Rest, esse anotation
 efetua um pré processamento ao compila essa clase*/
@RequestMapping(value = "/categories") /*Aqui você passa a rota*/
public class CategoryResource {


    @Autowired
    private CategoryService service;


    /*Response Entity é um objeto do spring que vai encapsular o resultado da
    requisição
     */
    @GetMapping
    public ResponseEntity <List<CategoryDTO>> findAll() {



     /* Lista mockada
       List<Category> list = new ArrayList<>(); /*
        List é uma interface não podemos implementar uma interface
        por isso implementamos o Array list que é uma classe que implementa
        o List
        list.add(new Category(1L, "Books")); // L representa que o número vai ser long
        list.add(new Category(2L, "Eletronics"));
 */
        List<CategoryDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
        /* .body  é para definir o corpo da resposta você pode colocar
        * o list dentro do ok, porque ele tem uma sobrecarga dentro do ok
        * que aceita o corpo*/

    }

    @GetMapping(value = "/{id}")
    /*path variable é o parâmetro que você vai passa na rota*/
    public ResponseEntity<CategoryDTO> findByid(@PathVariable Long id) {
        CategoryDTO dto  = service.findById(id);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {

        dto = service.insert(dto);

        /*Objeto que pega o endereço do objeto criado*/
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id")
                .buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
        /*create retorna 201 que é o código mais correto de recurso criado
        convém você também retornar o Location que é o endereço
         desse novo recurso criado na aba header do postman na requisição que mostrar o local
        de onde 201 http location header*/

    }

    @PutMapping(value = "/{id}")/*Put é um método não idepotente,
    salvar recurso de forma idempotente (se existir o produto do mesmo id, ele salva  o mesmo produto e não salva outro)*/
    public ResponseEntity<CategoryDTO> update (
            @PathVariable Long id,
            @RequestBody CategoryDTO dto) {

        dto = service.update(id, dto);

        return ResponseEntity.ok().body(dto);

    }
}