package com.hen.aula.entities;

import com.hen.aula.projections.IdProjection;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_product")
public class Product implements IdProjection<Long> {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT") /*para o atributo definition
    aceitar textos grandes*/
    private String description;
    private Double price;
    private String imgUrl;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant date; /* para aprender como colocamos
    data aqui é no padrão 8601, no size*/

    /*Set é um cojunto não aceita repetição
    e como muitos para muitos tem uma tabela de associação (que tem a
     chave primaria de categoria e do produto relacionando qual produto
     faz parte de qual categoria, um produto pode ter uma ou mais categoria
     ai mostra o id do produto em duas categori)para ela
    n reptir os valores usamos*/

    @ManyToMany
    @JoinTable(
            name = "tb_product_category",
            joinColumns = @JoinColumn(name = "product_id"), /*@JoinColun estabelece qual vai ser aa chave
            estrangeira da classe onde eu estou*/
            inverseJoinColumns =  @JoinColumn(name = "category_id") /*
            Classe associada  a essa */
    )
    private Set<Category> categories = new HashSet<>();


    public Product() {

    }

    public Product(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    /*Não tem set porque eu n troco a coleção de categorias, eu apenas
    * adiciono ou remove itens da categoria existent*/
    public Set<Category> getCategories() {
        return categories;
    }

}



