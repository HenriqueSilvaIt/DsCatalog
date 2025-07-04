package com.hen.aula.resources.exceptions;

import com.hen.aula.services.expections.DatabaseException;
import com.hen.aula.services.expections.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice /*isso vai permitir que essa classe intecepte
alguma exceção que ocorrer na camada controle resourc*/
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) /*Esse método
    vai interceptar o controle e lançar uma execeção ai você coloca essa anotation
    para ele interceptar e a exceção*/
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        /*Esse métodos recebe 2 parametros uma execeção e o http(especial do java web) que tem as informações da requisição*/

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value()); /* pega o número do erro http 404 que é o not found*/
        err.setMessage(e.getMessage()) /*pega a mensagem da nossa execeção customizada*/;
        err.setPath(request.getRequestURI()); /*ele pega o caminho da
        requisição que nós fizemos*/

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err); /*
        o método status nós colocamos o status not found 40 e o corpo da resporta
         que é objeto err do standard error*/
    }

    @ExceptionHandler(DatabaseException.class) /*Esse método
    vai interceptar o controle e lançar uma execeção ai você coloca essa anotation
    para ele interceptar e a exceção*/
    public ResponseEntity<StandardError> entityNotFound(DatabaseException e, HttpServletRequest request) {
        /*Esse métodos recebe 2 parametros uma execeção e o http(especial do java web) que tem as informações da requisição*/

        HttpStatus status = HttpStatus.BAD_REQUEST; /*para n repetir código*/

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value()); /*  400 é o código normal de erro genérico, quando voce n quer especifica*/
        err.setMessage(e.getMessage()) /*pega a mensagem da nossa execeção customizada*/;
        err.setPath(request.getRequestURI()); /*ele pega o caminho da
        requisição que nós fizemos*/

        return ResponseEntity.status(status).body(err); /*
        o método status nós colocamos o status not found 40 e o corpo da resporta
         que é objeto err do standard error*/
    }

}
