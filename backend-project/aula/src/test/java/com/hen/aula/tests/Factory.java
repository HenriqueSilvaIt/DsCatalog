package com.hen.aula.tests;

import com.hen.aula.dto.CategoryDTO;
import com.hen.aula.dto.ProductDTO;
import com.hen.aula.entities.Category;
import com.hen.aula.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Phone", "Go Phone", 800.00, "http:/img.cm" );

        product.getCategories().add(new Category(2L, "Eletronics"));

        return product;
    }

    public static Category createCategory() {

      return  new Category(2L, "Eletronics");


    }



    public static ProductDTO createProductDTO() {
        Product product = createProduct();

        return new ProductDTO(product, product.getCategories());    /*
        Tem um construtor que pega product e categoria dos produtos*/
    }

}
