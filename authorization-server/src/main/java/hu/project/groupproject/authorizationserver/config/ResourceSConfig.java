package hu.project.groupproject.authorizationserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import hu.project.groupproject.authorizationserver.CustomAuthThings.JwtToPrincipalConverter;
import hu.project.groupproject.authorizationserver.CustomAuthThings.MyJwtAuthenticationConverter;
import hu.project.groupproject.authorizationserver.filters.LoggingFilter;

@Configuration
@PropertySource("classpath:application.properties")
public class ResourceSConfig {
    @Value("${myVariables.jwk-set-uri}")
    String jwkSetUri;
    
    @Autowired
    JwtDecoder jwtDecoder;

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
                    .authenticationManager(new ProviderManager(authenticationProvider()))
                    // .authenticationManager(new ProviderManager(new JwtAuthenticationProvider(jwtDecoder)))
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
    public AuthenticationProvider authenticationProvider(){
        JwtAuthenticationProvider au = new JwtAuthenticationProvider(jwtDecoder);
        au.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return au;
        
    }


    
    @Bean
    public MyJwtAuthenticationConverter jwtAuthenticationConverter() {
        MyJwtAuthenticationConverter jwtAuthenticationConverter = new MyJwtAuthenticationConverter(new JwtToPrincipalConverter());
        // jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter());
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
