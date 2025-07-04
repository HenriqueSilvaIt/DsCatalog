package com.hen.aula.resources;

import com.hen.aula.CategoryDTO;
import com.hen.aula.entities.Category;
import com.hen.aula.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}