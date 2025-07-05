package com.hen.aula.dto;

import com.hen.aula.entities.Category;
import com.hen.aula.entities.Product;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {

    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        date = entity.getDate();
    }



    /*Sobrecarga mesmo construtor com outros argumento*/
    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity); /*chama o construtor que tem só  a entidade produto
        e todos os atributos dele*/
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
        /*para cada categoria do argumento, pegamos a categoria do atributo dessa classe e criamos um novo dto
        * para categoria */
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }
}
