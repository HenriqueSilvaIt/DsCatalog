package com.hen.aula.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {

	@Value("${cors.origins}")
	private String corsOrigins; /* cria a variavel
	CORS origins e depois configura ela lá em baixo par*/

	@Bean
	@Profile("test")
	@Order(1) /*método para funcionar o H2 com spring security
	profile é test porque rodamos o H2 só no ambiente de teste*/
	public SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {

		http.securityMatcher(PathRequest.toH2Console()).csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); /*
				frame disable é para n travar interface gráficca do H2*/
		return http.build();
	}

	@Bean
	@Order(3)
	public SecurityFilterChain rsSecurityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());/*
        crf desabilitando a proteção contra ataques csrf
        que é ataque contra aplicação que guarda estado na seção
        mas como aqui é uma API REST e api rest não guarda estado em seção
        estamos desabilitando isso*/
		http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
		/*anyRequest() tem como colocar restrição por rota se colocar authenticated() voc diz
		que tudo precisa estar logado, porém
		 * como essa classe trata de toda a aplicação, é mais recomendado
		 * configurar a restrição de rota no próprio end point*/
		http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		return http.build();
	}

	@Bean /*customização do token jwt para que funcione no resource serve*/
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
		grantedAuthoritiesConverter.setAuthorityPrefix("");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		String[] origins = corsOrigins.split(","); /*split com, para colocar
		 lá no aplication.properties os frontend permitdo
		 separado por virgul*/
		/*CORS é um recurso que tem no navegador
		 * que ele não deixa um recurso que não esteja associado
		 * acessar seu backend*/

		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOriginPatterns(Arrays.asList(origins)); /*pega
		a variavel origins que criamos la em cim e seta as origins permitida*/
		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
		/*acima ele seta os métodos permitido*/
		corsConfig.setAllowCredentials(true);
		/*vai dizer que podemos usar credenciais*/
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		/*acima é as informaões do cabeçalho*/

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}

	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
				new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE); /*esse método
		só fala que esse moutro método corsConfigurationSource deve
		 ser iniciado com a precedência máxima*/
		return bean;
	}
}
