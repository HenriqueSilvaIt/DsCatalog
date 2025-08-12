package com.hen.aula.services.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserInsertValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

/*Essa é uma classe que vai ser uma anotation
* esse código é um padrão para criar anotion de validação
*  nome da classe/anotion é sempre  com o sufixo Valid
*  e o nome  do validatedBy dentro da anotation @Constrantion
* vai ser o mesmo nome da classe com o sufixo Valitaor */
public @interface UserInsertValid {
    String message() default "Validation error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
