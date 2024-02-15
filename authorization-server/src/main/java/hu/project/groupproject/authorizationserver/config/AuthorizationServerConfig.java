package hu.project.groupproject.authorizationserver.config;

import java.time.Duration;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



@Configuration
public class AuthorizationServerConfig {
    @Autowired
    DataSource dataSource;
    // @Autowired
    // MyAuthenticationProvider myAuthenticationProvider;

    // TODO: register security rules/filters in the correst order something like
    // this:
    // (https://github.com/spring-projects/spring-security/blob/6.2.1/config/src/main/java/org/springframework/security/config/annotation/web/builders/FilterOrderRegistration.java)
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // http.authorizeHttpRequests(
        //     auth->auth
        //             .anyRequest().authenticated()
        //     );
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        http.exceptionHandling(c -> c
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
        http.sessionManagement(session -> {
            session
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER);
        });

        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain userManagementSecurityFilterChain(
            HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authz -> authz

                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/.well-known/**"))
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/error/**"))
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**"))
                        .permitAll()
                        .anyRequest().authenticated()
                        // .anyRequest().permitAll()
                        )
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .csrf(csrf -> csrf
                        // .ignoringRequestMatchers(
                        // AntPathRequestMatcher.antMatcher("/h2-console/**"))
                        // Apply CSRF protection only to /login/**
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/**"))
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/login/**")))
                // TODO:set the form login to custom page
                .formLogin(Customizer.withDefaults()).cors(Customizer.withDefaults())
                // .authenticationManager(new ProviderManager(myAuthenticationProvider))
                .passwordManagement(Customizer.withDefaults());
        return http.build();
    }

    // TODO:create JWKSource

    // @Bean
    // public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    //     return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    // }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                // .authorizationEndpoint("/myLoginPage")
                .build();
    }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // }

    
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedOrigin("http://localhost:8082");
        config.addAllowedOrigin("http://localhost:8083");
        config.addAllowedMethod("*"); // Allow all HTTP methods
        config.addAllowedHeader("*"); // Allow all headers
        config.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)
        source.registerCorsConfiguration("/**", config); // Apply CORS to all paths
        
        return new CorsFilter(source);
    }
    
    // TODO:possible improvement (priority:low): change in memory clientRepo to
    // jdbc/JPA in database
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient.withId("1")
        .clientId("myClient")
        .clientSecret("{bcrypt}$2a$10$Veul2yB.g3A4hmtHbmnQeuwfJDrYSfwQ8nilh0f8d5f6tM1mGPPQ.")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .scope(OidcScopes.PROFILE)
        .scope(OidcScopes.OPENID)
        .redirectUri("http://localhost:8081/login/oauth2/code/myClient")
        .postLogoutRedirectUri("http://localhost:8081")
        .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofSeconds(3600L)).build())
        .build();
        
        return new InMemoryRegisteredClientRepository(client);
    }
    


}