package com.hen.aula.services;


import com.hen.aula.dto.ProductDTO;
import com.hen.aula.entities.Category;
import com.hen.aula.entities.Product;
import com.hen.aula.repositories.CategoryRepository;
import com.hen.aula.repositories.ProductRepository;
import com.hen.aula.services.expections.DatabaseException;
import com.hen.aula.services.expections.ResourceNotFoundException;
import com.hen.aula.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests { /*Simula
o comportamento do ProductSERVICE utilzando o repository
para obter o resultado*/



    @InjectMocks /**/
    private ProductService service;

    @Mock/*o repository nós não injetamos nós apenas criamos objetos para eles mockado */
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;


    private long existingId;

    private Long dependentId;

    private PageImpl<Product> page;

    private Product product;

    private ProductDTO productDTO;

    private Category category;

    private   PageImpl pag;

    private Long nonExistingId;

    @BeforeEach void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 3L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        productDTO = Factory.createProductDTO();
        page = new  PageImpl<>(List.of(product));
        pag = new PageImpl<>(List.of());

        when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        when(repository.findById(existingId)).thenReturn(Optional.of(product));

        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.existsById(existingId)).thenReturn(true);
        /*Mockito.times(1) você coloca quantas vezes eses método foi chamado
        se você colcoar 2 vezes, quer dizer que o teste espera que o método
         se chamado 2 vezes e vai testar isso
         Mockito.never() quer dizer que você espera que n foi chamado nenhuma vez
          */
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        when(repository.existsById(nonExistingId)).thenReturn(false);
  //      Mockito.doNothing()
        when(repository.existsById(dependentId)).thenReturn(true);

        doNothing().when(repository).deleteById(existingId);


        /*Mockito.when é usamos quando o método que retorna alguma coisa*/
        /*Mockito.doNothing()  usamos para metódo que n faz nada n retorna nada
        * e n lança exceção*/
    }

    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable page = PageRequest.of(1,   10);

        Page<ProductDTO> result = service.findAllPaged("", "", pag.getPageable());

        Assertions.assertNotNull(result);

        Mockito.verify(repository, Mockito.times(1)).findAll(page);
    }


    @Test
    public void findByIdShouldReturnProductDTOWhenExistingId() {

       ProductDTO result =  service.findById(existingId);
       Assertions.assertNotNull(result);

    }


    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {


        Assertions.assertThrows(ResourceNotFoundException.class, () -> {

            service.findById(nonExistingId);;
        });
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExist() {

        ProductDTO update = service.update(existingId, productDTO);

        Assertions.assertNotNull(update);

    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionIdDoesNot() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.update(nonExistingId, productDTO);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDepententId() {

        Assertions.assertThrows(DatabaseException.class, () ->  {

            service.delete(dependentId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExisting() {




        /*Vai chamar o delete do repository e não vai fazer nada*/

        /*Poderiamos até n colocar assertions porque n vai acontecer nada*/

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId); /*chama o método na classe
            service normal*/
        });
        /*Quando criamos um mock temos que  configurar o comportamento simulado dele
         * ou seja temos que configurar oque deveria acontecer no mock*/



    }

    /*
       @MockitoBean o Mock é usado quando utilizado um teste de unidade que não carrega
       o contexto, por exemplo quando é uma classe de teste de unidade
       que usamos o @ExtendWith(SpringExtension.class), nós vamos dar
       preferência ao mock

       Quando é uma classe de teste que carrega o contexto da aplicaçao
       e precisa mockar algum bin específico, algum do sistema, aí o usamos o mockbean/
    private ProductRepository repository2;*/

}
