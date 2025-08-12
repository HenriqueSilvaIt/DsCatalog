package com.hen.aula.services;

import com.hen.aula.dto.EmailDTO;
import com.hen.aula.dto.NewPasswordDTO;
import com.hen.aula.entities.PasswordRecover;
import com.hen.aula.entities.User;
import com.hen.aula.repositories.PasswordRecoverRepository;
import com.hen.aula.repositories.UserRepository;
import com.hen.aula.services.expections.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private PasswordRecoverRepository passwordRecoveryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    @Value("${email.password-recover.uri}")/*variavel
    do token no application.properties*/
    private String recoverUri;

    @Value("${email.password-recover.token.minutes}")/*variavel
    do token no application.properties*/
    private Long tokenMinutes;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Transactional
    public void saveNewPassword(NewPasswordDTO body) {
        /*verificar se existe um token válido*/

        List<PasswordRecover> result = passwordRecoveryRepository.searchValidTokens(body.getToken(), Instant.now());
        if(result.size() == 0) {
            /*se n retornar nenhum token válido na lista de token n expirado*/
            throw new ResourceNotFoundException("Token inválido");
        }

        User user = userRepository.findByEmail(result.get(0).getEmail()); /*
        pegando o usuário , get(0 pegando o primeiro token que chegou*/

        /*colocando a nova senha que usuário digitou e criptografando ela*/
        user.setPassword(passwordEncoder.encode(body.getPassword()));

        /*salvando usuário com nova senha*/

        user = userRepository.save(user);
    }


        /*Retorna o usuário logado*/

        protected User authenticated() {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                /*retorna um objeto do tipo authentication e partir dele
                * podemos pegar o getPrincipal abaixo como usamos e configuramos o token jwt
                * nós fazemo o casting abaixo (Jwt) */
                Jwt jwtPrincipal = (Jwt) authentication.getPrincipal(); /*pegamos o objeto Jwt
                */
                String username = jwtPrincipal.getClaim("username"); /*a partir do objeto
                Jwt podemos dar um getClaim para pegar o nome do usuário*/
                return userRepository.findByEmail(username); /*e retornamos o username
                , então estamos buscando uma entidade usuário a partir do email que estáva lá no token
                do usuário que está autenticado na requisição acessando o backend*/
            }
            catch (Exception e) { /*se dar qualquer erro caso o usuário n esteja
            autenticado ou alguma coisa vai dar o erro abaixo*/
                throw new UsernameNotFoundException("Invalid user");
            }
        }
}
