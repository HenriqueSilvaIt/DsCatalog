package com.hen.aula.services.validation;

import com.hen.aula.dto.UserUpdateDTO;
import com.hen.aula.entities.User;
import com.hen.aula.repositories.UserRepository;
import com.hen.aula.resources.exceptions.FieldMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> /*Essa
 classe é generics e recebe 2  tipos, o nome da sua Anotation no 1º, no 2º argumento
 o nome da classe que está recebendo esse anotation de  validação*/{

     @Autowired
     private HttpServletRequest request; /*
     esse HttpServletRequest guarda
     as informações da requisição
     a partir dele é possível pegar o id que passei na rota*/

     @Autowired
     private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked") /*tira esse amarelinho que é um warning para
         typ safetity dessa conversão*/

        var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
/*getAttribute(pega os atributos.HandllerMapping (acessa as variaveis da url
URI_tEMPLATE_VARIABLE_ATTRIBUTE ele vai pegar um dicionario as variaveis da URL*/

        long userId = Long.parseLong(uriVars.get("id")); /*
        aqui ele pega o valor PASSado no id da url

        mas como esse Id é HTTP tem que converter ele pra long
        ai nós usamos o Long.parseLong()

        ele vai reclamar que o método get string não está definido para o tipo
        object, ai é preciso fazer um casting com Map<string, string>)*/


        List<FieldMessage> list = new ArrayList<>();/*aqui foi criado uma lista
        do FieldMessage que é aquela classe que tem os campos do erro HTTP e também a lista de erros de validação
        no http*/

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
    //nesse momento eu posso fazer qualquer teste no meu DTO por exemplo
        User user = repository.findByEmail(dto.getEmail());/*pega
        o email que está no dto que o usário digito na requisiçã*/

     if  (user != null && userId != user.getId()) {/*segunda condição
     valida se o id do usuário não for o Id do usuário que eu estou tentando atualizar
      quer dizer que eu estou atualizando um email que já exist*/
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