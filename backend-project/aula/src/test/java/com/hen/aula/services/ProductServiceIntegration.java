package com.hen.aula.services;

import com.hen.aula.dto.ProductDTO;
import com.hen.aula.repositories.ProductRepository;
import com.hen.aula.services.expections.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest /*Usamos essa pois é teste de integração
e essa anotatio carrega o contexto da aplicação*/
@Transactional
/*Testes tem que ser transacionais, depois de cada teste da classe integração o banco faz um rollback no banco para que em cada teste o banco
 fique na forma original antes de alteração de cadata teste, para isso é só colocar na classe Transacional*/
public class ProductServiceIntegration {


    @Autowired
    private ProductService service; /*Vamos
     injetar essa classe e automaticamente ele vai injeta
     a depência do repository que já esta inejtada nessa classe servic*/

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private   PageImpl page;
    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L; /*
        Quantidade de produto no si*/

        page = new PageImpl<>(List.of());
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() {


        service.delete(existingId);

        Assertions.assertEquals(countTotalProducts -1, repository.count());
        /* count do repository retorna a quantidade total de registro no banco
        * ai estamos testando se a quantidade de produto no banco
        * é total que temos de produto  - 1 que eu removi esse é o resultado esperado
        * de registr que ficou no banc*/

    }

@Test
public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {


        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);

        });

}

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() {

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result =  service.findAllPaged("", "", page.getPageable());

        Assertions.assertFalse(result.isEmpty());
        /*aSSERTfALSE PARA garantir que o resultado n está vazia
        * */

        Assertions.assertEquals(0, result.getNumber());
        /*Testa se está na página 0 */
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
        /*Testa o taotal de elementos na pagina*/

    }

    @Test
    public void findAllPagedShouldReturnEmptyWhenPageDoesNotExists() {
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = service.findAllPaged("", "", page.getPageable());

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    public void findAllPagedShouldReturnedSortedPageWhenSortByName() {

        PageRequest pageRequest = PageRequest.of(0,  10, Sort.by("name"));

        Page<ProductDTO> result = service.findAllPaged("", "", page.getPageable());


        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        /*testa se o nome do primeiro é o Macbook Pro que é que o vem antes
        * em ordem alfabética
        *
        * result.getContent() pega a lista da pagina .get(0) pega primeira posição
        * que é 0 e .getName( pega o nome do produto*/
    }



}
