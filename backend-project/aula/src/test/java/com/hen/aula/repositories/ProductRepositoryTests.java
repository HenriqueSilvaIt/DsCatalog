package com.hen.aula.repositories;


import com.hen.aula.entities.Product;
import com.hen.aula.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest /*Tem a infraestrutura do spring DATA jpa para acessar o banco*/
public class ProductRepositoryTests { /*
simula o comportamento dele mesmo com objetos mockado*/

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception {
         existingId = 1L;

         nonExistingId = 100L;


         countTotalProduct = 25L;
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalProductWhenIdDoesNotExist() {

        nonExistingId = 50L;

        repository.findById(nonExistingId);

        Optional <Product> result =  repository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());


    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalProductWhenIdDoesNotExist() {

        repository.findById(existingId);

        Optional <Product> result =  repository.findById(existingId);

        Assertions.assertTrue(result.isPresent());


    }


    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {


        //Arrange

        repository.deleteById(existingId);

        //Act
       Optional <Product> result =  repository.findById(existingId);

       //Assert

        Assertions.assertFalse(result.isPresent()); /* valida se existe
        um objeto dentro do optional*/



    }

    @Test
    public void deleteShouldThrowResultDataAccessExceptionWhenIdDoesNotExist() {


        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        Product product  = Factory.createProduct();

        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId()); /*Testa
        se o id do product.getId( n é nulo*/

        Assertions.assertEquals(countTotalProduct + 1, product.getId());    }
}



