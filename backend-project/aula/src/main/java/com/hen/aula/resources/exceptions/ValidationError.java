package com.hen.aula.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{

    private List<FieldMessage> errors = new ArrayList<>();

    /*método para retornar os errors da lista fieldMessage*/
    public List<FieldMessage> getErrors() {
        return errors;
    }

    /*método para adionar erros na lista field message*/
    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
