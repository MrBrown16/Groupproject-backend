package hu.project.groupproject.authorizationserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import hu.project.groupproject.authorizationserver.filters.LoggingFilter;

@Configuration
@PropertySource("classpath:application.properties")
public class ResourceSConfig {
    @Value("${myVariables.jwk-set-uri}")
    String jwkSetUri;
    

     @Bean
    @Order(2) //look at this 2 because i have spent approx 6 hours on debugging and this was the solution so appreciate it!
    public SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {


        //  http.oauth2ResourceServer(r->r.jwt(Customizer.withDefaults()));
        http.oauth2ResourceServer(oauth2ResourceServer ->
        oauth2ResourceServer
            .jwt(jwt ->
                jwt
                    
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
        );
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingFilter());
        registrationBean.addUrlPatterns("/*"); // Apply filter to all URLs
        return registrationBean;
    }
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }


    //duplicate unnecessary
    // @Bean
    // public JwtDecoder jwtDecoder() {
    //     // Configure the JWT decoder with the public key or certificate used for signature verification
    //     return NimbusJwtDecoder.withJwkSetUri(jwkSetUri) // Specify the public key
    //                            .build();
    // }


}
