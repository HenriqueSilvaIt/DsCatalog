package com.hen.aula.entities;

import com.fasterxml.jackson.annotation.JsonTypeId;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Objects;

public class Category {

    private Long id;
    private String name;

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
