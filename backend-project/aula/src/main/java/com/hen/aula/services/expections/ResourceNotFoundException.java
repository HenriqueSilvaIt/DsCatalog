package com.hen.aula.services.expections;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String msg) {
        super(msg); /*repasse o argumento
         para o construtor da  super classe */
    }
}
