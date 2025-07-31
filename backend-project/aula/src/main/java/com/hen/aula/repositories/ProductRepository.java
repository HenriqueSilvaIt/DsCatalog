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
            SELECT DISTINCT tb_product.id, tb_product.name
           FROM tb_product
           INNER JOIN tb_product_category
           ON tb_product.id = tb_product_category.product_id
           WHERE (:categoryIds IS NULL OR tb_product_category.category_id  in :categoryIds)
           AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%', :name, '%'))
           ORDER BY tb_product.name
            """, countQuery = """ 
           SELECT COUNT(*) FROM (
           SELECT DISTINCT(tb_product.id), tb_product.name
           FROM tb_product
           INNER JOIN tb_product_category
           ON tb_product.id = tb_product_category.product_id
           WHERE (:categoryIds IS NULL OR tb_product_category.category_id  in :categoryIds)
           AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%', :name, '%'))
           ORDER BY tb_product.name)
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
}
