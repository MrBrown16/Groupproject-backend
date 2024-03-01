package hu.project.groupproject.resourceserver.config;

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
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("authorities");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
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
