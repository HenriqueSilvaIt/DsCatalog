package com.hen.aula.config;


import com.hen.aula.config.customgrant.CustomPasswordAuthenticationConverter;
import com.hen.aula.config.customgrant.CustomPasswordAuthenticationProvider;
import com.hen.aula.config.customgrant.CustomUserAuthorities;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

	/*Esses 2 atributos abaixo, são registrados no método bean RegisteredClientRepository*/

	@Value("${security.client-id}")
	private String clientId;

	@Value("${security.client-secret}")
	private String clientSecret;




	/* A duração do token de 1 dia, que está descrito em segundo
	 é armazenada na variavel abaixo	e configura o token para durar essa duração
	 caso queira mudar o  tempo de duração do token é só mudar no applacation.propertie*/
	@Value("${security.jwt.duration}")
	private Integer jwtDurationSeconds;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;




	@Bean
	@Order(2) // Ele é ordem 2 porque tem outros desse cara no ResourceServer com order 1 3
	public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {

		//Habilitar o autorization
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http); /*
		Estamos configurando o authorization server (do oauth2), aplicando o objeto padrão
		 do Spring security (estamos configurando o authorization server para funcionar com o spring
		 security)*/



		// @formatter:off
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				// Token endpoint
				.tokenEndpoint(tokenEndpoint -> tokenEndpoint
						// Configuração de customização de token:
						.accessTokenRequestConverter(new CustomPasswordAuthenticationConverter())
						// Configuração de como será autenticação, , como ele vai ler as credenciais e gerar o tokeno:
						.authenticationProvider(new CustomPasswordAuthenticationProvider(authorizationService(), tokenGenerator(), userDetailsService, passwordEncoder))); /*
						tem o userDetailsservice aplicado que é o componente que busca o usuário, passando o nome do usuário
						,  e depois dele tem o password encoder (tem um método Bean do password para que ele funcione dentro do Spring security no oauth2)*/

		// Essa linha abaixo é para que ele funcione trabalhando com o token do JWT
		http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));
		// @formatter:on

		return http.build();
	}


	@Bean
	public OAuth2AuthorizationService authorizationService() {
		return new InMemoryOAuth2AuthorizationService();
	}

	@Bean
	public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService() {
		return new InMemoryOAuth2AuthorizationConsentService();
	}


	/*Método que entrar de forma entregado com o spring security
	*    */


	/*Registra  a aplicação cliente de acordo com o clientid e clientsecret passado*/
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		// @formatter:off
		RegisteredClient registeredClient = RegisteredClient
				.withId(UUID.randomUUID().toString())
				.clientId(clientId)
				.clientSecret(passwordEncoder.encode(clientSecret))
				.scope("read")
				.scope("write")
				.authorizationGrantType(new AuthorizationGrantType("password")) // isso é o nome do grantype
				// por isso que colocamos grantype password no gratype na hora de passar
				.tokenSettings(tokenSettings())
				.clientSettings(clientSettings())
				.build();
		// @formatter:on

		return new InMemoryRegisteredClientRepository(registeredClient);/*
		ele gera um objeto em mémoria com os dados client id e cliend secret, ele n tem um banco
		 ele só gera em memória, se  client id e client secret da aplicação tiver errado
		 n vai gerar o token, vai mostrar usuário inválido*/
	}

	@Bean
	public TokenSettings tokenSettings() {
		// @formatter:off
		return TokenSettings.builder()
				.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
				.accessTokenTimeToLive(Duration.ofSeconds(jwtDurationSeconds))
				.build();
		// @formatter:on
	}

	@Bean /*só para instanciar as configurações da aplicação cliente */
	public ClientSettings clientSettings() {
		return ClientSettings.builder().build();
	}


	// é outro BEAN do spring security oauth2 para fazer funcionar
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	//	Faz alguma configuração de token para nós
	@Bean
	public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
		NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
		JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
		jwtGenerator.setJwtCustomizer(tokenCustomizer());
		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
		return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator);
	}

	/* Método que customiza o token gerando os claim (o JWT tem o conceito de claim (reivindicação),
	*     o token está reivindicando que o usuário do meu token é
	* .claim("username", user.getUsername()); esse e o
	* .claim("authorities", authorities) e as autoridade (perfis) são essas
	*  para authorization server aceitar essas revindicações, ele vai verificar
	*  se o token n está expirado e se a chave RSA privada vai bate também */
	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
		return context -> {
			OAuth2ClientAuthenticationToken principal = context.getPrincipal();
			CustomUserAuthorities user = (CustomUserAuthorities) principal.getDetails();
			List<String> authorities = user.getAuthorities().stream().map(x -> x.getAuthority()).toList();
			if (context.getTokenType().getValue().equals("access_token")) {
				// @formatter:off
				context.getClaims()
						// eu posso passar outro claim aqui .claim("nome", "Henrique"), ai
						// vai gerar o token com a informação do meu nome
						// é possível ver isso gerando o token e consultand no decoder do jwt.io
						.claim("authorities", authorities)
						.claim("username", user.getUsername());
				// @formatter:on
			}
		};
	}


	//JWT decoder
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	/*O token jwt é assinado, o Authotization server vai gerar um token
	 * embaralhando os dados deles, usando uma criptografia assímetrica que é o RSa, para embaralhar
	 * com uma chave privada, que só o authorization server conhecer (existe também uma
	 * chave pública
	 *
	 * Esses 3 métodos abaixo ,faz  a configuração do algoritmo  do RSA, que vai gerar
	 * a chave pr embaralhar junto com o token, por isso token é seguro, porque nenhuma aplicação
	 * vai conseguir gerar um token que o authorization server da minha aplicação vai
	 * reconhecer como válido*/

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	private static RSAKey generateRsa() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
	}

	private static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}
}