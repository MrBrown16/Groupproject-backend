package hu.project.groupproject.resourceserver.config;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer.JwtConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                // .anyRequest().permitAll()
                                .anyRequest().authenticated()
                                
                                ).oauth2ResourceServer(
                                    oauth2 -> oauth2
                                    .jwt(
                                        Customizer.withDefaults()
                                    )
                                )
                                // .anyRequest().authenticated()).oauth2ResourceServer(
                                //     oauth2 -> oauth2
                                //     .jwt(
                                //         jwt -> jwt.jwkSetUri("http://localhost:8083/oauth2/jwks")
                                //     )
                                // )
                // .httpBasic(Customizer.withDefaults())
                // .cors(Customizer.withDefaults())
                .csrf(
                    csrf->csrf
                    .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                    .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/img/**"))
                )
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable()) //h2-console requires it
                .sessionManagement(session -> {
                    session
                            .sessionCreationPolicy(SessionCreationPolicy.NEVER);
                });
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("authorities");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthenticationConverter;
    }


//     @Bean
//     public SecurityFilterChain securityFilterChain(
//            HttpSecurity http
//         ) throws Exception {
//        http.authorizeHttpRequests(
//                authz -> authz
//                        .anyRequest().access(new AuthorizationManager<RequestAuthorizationContext>() {
//                            @Override
//                            public AuthorizationDecision check(
//                                    Supplier<Authentication> authentication,
//                                    RequestAuthorizationContext object
//                            ) {
//                                Set<String> authorities = authentication.get().getAuthorities()
//                                        .stream()
//                                        .map(GrantedAuthority::getAuthority)
//                                        .collect(Collectors.toSet());
//                                System.out.println(authorities);
//                                return new AuthorizationDecision(
//                                        authorities.contains("ROLE_ADMIN")
//                                );
//                            }
//                        })
//        );
//        return http.build();
//    }

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
