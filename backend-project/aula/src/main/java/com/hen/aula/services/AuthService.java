package com.hen.aula.services;

import com.hen.aula.dto.EmailDTO;
import com.hen.aula.entities.PasswordRecover;
import com.hen.aula.entities.User;
import com.hen.aula.repositories.PasswordRecoveryRepository;
import com.hen.aula.repositories.UserRepository;
import com.hen.aula.services.expections.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

        @Autowired
        private PasswordRecoveryRepository passwordRecoveryRepository;

    @Value("${email.password-recover.uri}")/*variavel
    do token no application.properties*/
    private String recoverUri;

    @Value("${email.password-recover.token.minutes}")/*variavel
    do token no application.properties*/
    private Long tokenMinutes;

    @Transactional
    public void createRecoverToken(EmailDTO body) {

        User user = userRepository.findByEmail(body.getEmail()); /*verificar
        se o usuário que o cliente digitou existe no banco de dados
        se não existir vai retornar nulo ai tem que lançar exceção*/


        /*Verifica se o email que o usuário digitou existe no banco*/
        if (user == null) {

            throw new ResourceNotFoundException("Email não encontrado");
        }

        String token = UUID.randomUUID().toString();/*UUID cria um código
        aleatório que tem um monte de letra, toString é para converte-lo para string*/


        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail()); /*EMAIL QUE o usuário digitou
        para recuperar senha*/
        entity.setToken(token);/*UUID cria um código
        aleatório que tem um monte de letra, toString é para converte-lo para string*/

        /*Abaixo o tempo de expiração de token*/
        entity.setExpirationToken(Instant.now().plusSeconds(tokenMinutes * 60L)
        /*ele n tem plus de minutos soment de segundos por isso fazemos o tokenMinutos * 60*/);


        /*salvando no banco de dados o email que o usário digitou
        * e token aletário com 30 min de validade ou outra coisa*/
        entity = passwordRecoveryRepository.save(entity);

        /*preparando informações e enviando
        * email para o usuário criar uma nova senha*/
        String text = "Acesse o link para definir uma nova senha \n\n"
                + recoverUri + token+  ". Validade de " + tokenMinutes +
                " minutos"; /*a URL do frontend
                + o código do token gerado isso vai ser o link*/

        emailService.sendEmail(body.getEmail(), "Recuperação de senha",
                text) /*to, subject, body*/;
    }

}
