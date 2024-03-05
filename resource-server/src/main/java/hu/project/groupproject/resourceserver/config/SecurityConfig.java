package hu.project.groupproject.resourceserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import hu.project.groupproject.resourceserver.CustomAuthThings.JwtToPrincipalConverter;
import hu.project.groupproject.resourceserver.CustomAuthThings.MyJwtAuthenticationConverter;
import hu.project.groupproject.resourceserver.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @PersistenceContext
    EntityManager manager;


    @Autowired
    UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                // .anyRequest().permitAll()
                                .anyRequest().authenticated()
                                
                                ).oauth2ResourceServer(
                                    oauth2 -> oauth2
                                    .jwt(jwt->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                                        // Customizer.withDefaults()
                                    )
                                )                
                .csrf(
                    csrf->csrf
                    .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                    // .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/img/**"))
                )
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable()) //h2-console requires it
                .sessionManagement(session -> {
                    session
                            .sessionCreationPolicy(SessionCreationPolicy.NEVER);
                });
        return http.build();
    }

    @Bean
    public MyJwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter gConverter = new JwtGrantedAuthoritiesConverter();
        JwtToPrincipalConverter pConverter = new JwtToPrincipalConverter();
        pConverter.setUserService(userService);
        pConverter.setManager(manager);
        // converter.setAuthorityPrefix("");
        // converter.setAuthoritiesClaimName("authorities");
        MyJwtAuthenticationConverter jwtAuthenticationConverter = new MyJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(gConverter);
        jwtAuthenticationConverter.setJwtToPrincipalConverter(pConverter);
        return jwtAuthenticationConverter;
    }

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


}
