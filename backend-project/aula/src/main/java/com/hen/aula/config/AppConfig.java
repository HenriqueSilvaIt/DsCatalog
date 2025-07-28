package com.hen.aula.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration /*registra como configura
 o spring instancia esse cara e gerencia injeção dessa dependência em outros
 componentes(clase)*/
public class AppConfig {

    @Bean/*é uma anotation de método, que diz que essa instancia
    vai ser um component gerenciado pelo spring boot
    então o meto fica tipo uma classe, ele pode ser injetado em
    outra classe/componente*/
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
