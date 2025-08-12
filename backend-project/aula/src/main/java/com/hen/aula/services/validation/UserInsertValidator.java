package com.hen.aula.services.validation;

import com.hen.aula.dto.UserInsertDTO;
import com.hen.aula.entities.User;
import com.hen.aula.repositories.UserRepository;
import com.hen.aula.resources.exceptions.FieldMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> /*Essa
 classe é generics e recebe 2  tipos, o nome da sua Anotation no 1º, no 2º argumento
 o nome da classe que está recebendo esse anotation de  validação*/{

     @Autowired
     private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();/*aqui foi criado uma lista
        do FieldMessage que é aquela classe que tem os campos do erro HTTP e também a lista de erros de validação
        no http*/

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
    //nesse momento eu posso fazer qualquer teste no meu DTO por exemplo
        User user = repository.findByEmail(dto.getEmail());/*pega
        o email que está no dto que o usário digito na requisiçã*/

     if  (user != null) {
         list.add(new FieldMessage("email", "Email já existe"));
     }

        for (FieldMessage e : list) {/*percorre  a minha lista do fieldMessage para inserir na lista de erros do Bean Validations*/
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();/*esse retorno
        testa se a lista está vazia, se retorna vazia quer dizer que nenhum dos testes que fiz acima deu erro
        se tiver vazia é verdadeiro, agora se tiver algo deu erro e vai retornar falso e mostar exceção*/
    }
}