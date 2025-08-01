package com.hen.aula.repositories;

import com.hen.aula.entities.Product;
import com.hen.aula.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    /*Consulta para buscar os Ids dos produtos que vão fazer parte da página*/

    @Query(nativeQuery = true, value = """
           SELECT * FROM (
           SELECT DISTINCT tb_product.id, tb_product.name
           FROM tb_product
           INNER JOIN tb_product_category
           ON tb_product.id = tb_product_category.product_id
           WHERE (:categoryIds IS NULL OR tb_product_category.category_id  in :categoryIds)
           AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%', :name, '%'))
           ) AS tb_result
            """, countQuery = """ 
           SELECT COUNT(*) FROM (
           SELECT DISTINCT(tb_product.id), tb_product.name
           FROM tb_product
           INNER JOIN tb_product_category
           ON tb_product.id = tb_product_category.product_id
           WHERE (:categoryIds IS NULL OR tb_product_category.category_id  in :categoryIds)
           AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%', :name, '%')))
           AS tb_count_result
           """) /*:name é para pegar a variavel
            que está sendo passada pelo usuário no argumento do método*/
    /*essa outra consulta é a mesma só com o count, para contar a quantidade
    * de produtos no resultado dessa consulta, para não dar o prolema da N+!
    * consulta*/
    Page<ProductProjection> searchProduct(List<Long> categoryIds, String name, Pageable pageble);
    /*normalmente colocamos os nomes dos métodos no repositório
    * começando com search porque os métodos que já vem no jpa
    * repository começando com find e os QueryMethods também*/


    /*Consulta para buscar os produtos com as categorias pegando o id do produto com o
    * resultado da consulta anterior*/


    @Query("SELECT obj " +
            "FROM Product obj " +
            "JOIN FETCH obj.categories " +
            "WHERE obj.id IN :productIds " +
            "ORDER BY obj.name")/*dentro da classe product
            o nome do atributo da classe categoria é categories
            por isso passamos assim na query*/
    List<Product> searchProductWithCategories(List<Long> productIds);
}
