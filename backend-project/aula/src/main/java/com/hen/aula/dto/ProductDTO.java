package com.hen.aula.dto;

import com.hen.aula.entities.Category;
import com.hen.aula.entities.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;

    @Size(min = 5, max = 60, message = "Deve ter entre 5 e 60 caracteres")
    @NotBlank(message = "Campo requirido")
    private String name;
    @NotBlank(message = "Campo requirido")
    private String description;
    @Positive(message = "Preço deve ser positivo")
    private Double price;
    private String imgUrl;
    @PastOrPresent(message = "A data do produto não pode ser futura")
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



    /*Sobrecarga mesmo construtor com outros argumento
    * Caso vocÊ queira carregar o produtos com as categorias associadas
    * ao produto você usa esse construtor que tem 2 argumento no service
    * se você quiser carregar só os produto você usa o construtor acim*/
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
