package com.hen.aula.services;

import com.hen.aula.services.expections.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Value("${spring.mail.username}") /*Esse username é oque
	configuração no properties e usa esse valor
	como se fosse o rementente aquele que está enviando o email
	e remetent fica nessa varaivel emailFrom*/
	private String emailFrom;
	
    @Autowired
    private JavaMailSender emailSender;/*
    Esse JavaEmail sender é da biblioteca spring-boot-stater-mail
    tem que usar a dependencia
           <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
       Essa bibliotec tem os componentes basicos
       para enviar email usando o framework do spring

       Esse JavaMail sender pega toda configuração que temos
       de email no propertie*/

    public void sendEmail(String to, String subject, String body) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();/*é um
            objeto de mensagem de lib do spring, ele vai receber
            as informações do DTO*/
            message.setFrom(emailFrom);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
        }
        catch (MailException e){
        	throw new EmailException("Failed to send email");
            /*caso de algum erro no envio do email, email
            * exception é uma exceção customizada e tem também
            *  o exception handler para lança uma exceção http customizada se der
            * erro*/
        } 
    }
}
