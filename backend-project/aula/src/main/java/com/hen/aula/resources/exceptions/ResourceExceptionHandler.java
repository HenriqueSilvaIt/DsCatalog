package com.hen.aula.resources.exceptions;

import com.hen.aula.services.expections.DatabaseException;
import com.hen.aula.services.expections.EmailException;
import com.hen.aula.services.expections.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        err.setError("ResourceNotFound exception");

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
        err.setError("DabaseException exception");


        return ResponseEntity.status(status).body(err); /*
        o método status nós colocamos o status not found 40 e o corpo da resporta
         que é objeto err do standard error*/
    }


    @ExceptionHandler(MethodArgumentNotValidException.class) /*Esse método
    vai interceptar o controle e lançar uma execeção ai você coloca essa anotation
    para ele interceptar e a exceção*/
    public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        /*Esse métodos recebe 2 parametros uma execeção e o http(especial do java web) que tem as informações da requisição*/

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; /*para n repetir código
        esse status ele diz que algum argumento n foi processado
        algumas pessoas usam esse status para validação */

        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value()); /*  400 é o código normal de erro genérico, quando voce n quer especifica*/
        err.setMessage(e.getMessage()) /*pega a mensagem da nossa execeção customizada*/;
        err.setPath(request.getRequestURI()); /*ele pega o caminho da
        requisição que nós fizemos*/
        err.setError("Validation exception");

        for (FieldError f : e.getBindingResult().getFieldErrors()) {
    /*  Essa referência pega os erros da validação, e o getFieldError
        traz uma lista de errors dos campos da validação*/

            err.addError(f.getField(), f.getDefaultMessage());
            /*FieldError é uma classe padrão do Bean Validation
            * e você consegue pegar o nome de campo e também
            * a messagem customizada*/

        }
        return ResponseEntity.status(status).body(err); /*
        o método status nós colocamos o status not found 40 e o corpo da resporta
         que é objeto err do standard error*/
    }

    /*exceção envio de email*/
    @ExceptionHandler(EmailException.class) /*Esse método
    vai interceptar o controle e lançar uma execeção ai você coloca essa anotation
    para ele interceptar e a exceção*/
    public ResponseEntity<StandardError> entityNotFound(EmailException e, HttpServletRequest request) {
        /*Esse métodos recebe 2 parametros uma execeção e o http(especial do java web) que tem as informações da requisição*/

        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value()); /* pega o número do erro http 400 que é o not found*/
        err.setMessage(e.getMessage()) /*pega a mensagem da nossa execeção customizada*/;
        err.setPath(request.getRequestURI()); /*ele pega o caminho da
        requisição que nós fizemos*/
        err.setError("ResourceNotFound exception");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err); /*
        o método status nós colocamos o status not found 40 e o corpo da resporta
         que é objeto err do standard error*/
    }


}
