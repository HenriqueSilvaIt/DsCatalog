package com.hen.aula.entities;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity/*sempre importa com base na especificação do jakarta, se amanhã trocarmos a implementação trocarmos o hibernate por outra
a aplicação vai continuar funcionando */
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY) /*Configura o Id
    para ser auto incrementado*/
    private Long id;
    private String name;


    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    /*quando vocÊ coloca TIMESTAMP WITHOUT TIME ZONE sem especificar
    * qualquer timezone, vai ser sempre o horário UTC de londres
    *  ai quando precisa converte no service para o horário
    * local do cliente, o professor indica sempre dar preferencia
    *  deixar without time zone sem especificar um fusuori
    * Tem que criar o get do atributo abaix, esse atributo
    * vai ser alterado somente quando você criar e ele n
    * vai ser mais atualizado*/
     private Instant createdAt; /*armazena qual o instant que  algum
      atributo dessa classe foi criado pela primeira ve */


        /*Lista de produto associado a categori*/
        @ManyToMany(mappedBy = "categories") /*mappedby
        vai fazer o mapeamento do outro lado com base no que já está feito
        do outro, tem que coloca o mesmo nome do atributo dessa classe
        na classe associada

        COM ESSa associação eu vou conseguir acessar as categorias e dar um
        getProducts para pegar os produtos associados a essa categori*/
        private Set<Product> products = new HashSet<>();

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt; /* armazena o instant em que algum
    atributo dessa clas    foi atualziada */

    public  Category() {

    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /*Método quando salvar uma categoria
    ele vai armazenar no createdAt (Atibuto)  o instant atual
    e quando eu atualizar uma categoria ele armazena no updatedAt
     */

    /*PrePersisti é porque antes de persist antes de salvar no banco
    vairealizar essa ação
     */
    @PrePersist /*Sempre que você der um save do jpa pela primeira vz
     no banco de dados ele vai chamar o pre persisti e vai salvar o momento
     que foi criado*/
    public void prePersist () {
        createdAt = Instant.now();
    }

    @PreUpdate /*Sempre que você atualizar
     no banco de dados ele vai chamar o pre updated e vai atualizar o
     atributo do updated AT*/
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    /*Temos 2 métodos de comparação, porque o Hashcode é bem rápido
    * se você precisar varrer uma lista comparando ele é rápido
    * mas se vocÊ encontrar algum código igual que o hashcode gerou( é dificil
    * mais pode acontecer, ai você usa o equals*/

        /*Equals também é um método de comparação que qualquer objeto java
        * pode ter, a comparação do equals é mais lenta, e você define o critério
        * de comparação, ou ele gera automatico abaixo, diferente do  hashcode
        * ele sempre vai gerar um código único*/

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    /*Hash  é um método padrão que todo objeto java pod ter
     para comparar se um objeto é igual a outro, basicamente
     ele compara se os números são iguais, porém o hashCode tem uma chance
      muito pequena de gerar o mesmo número o mesmo código*/

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
