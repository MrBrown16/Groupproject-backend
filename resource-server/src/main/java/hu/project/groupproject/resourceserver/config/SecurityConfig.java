package hu.project.groupproject.resourceserver.config;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer.JwtConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz ->
                        authz
                                .anyRequest().authenticated()).oauth2ResourceServer(
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
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> {
                    session
                            .sessionCreationPolicy(SessionCreationPolicy.NEVER);
                });
        return http.build();
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
}
