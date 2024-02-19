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
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import hu.project.groupproject.authorizationserver.CustomAuthThings.JwtToPrincipalConverter;
import hu.project.groupproject.authorizationserver.CustomAuthThings.MyAuthenticationProvider;
import hu.project.groupproject.authorizationserver.CustomAuthThings.MyJwtAuthenticationConverter;
import hu.project.groupproject.authorizationserver.filters.LoggingFilter;

@Configuration
@PropertySource("classpath:application.properties")
@Order(80)
public class ResourceSConfig {
    @Value("${myVariables.jwk-set-uri}")
    String jwkSetUri;
    @Autowired
    JwtDecoder jwtDecoder;
    // @Autowired
    MyAuthenticationProvider myAuthenticationProvider;
    // UserDetailsManager userDetailsManager;


    // public ResourceSConfig(JwtDecoder jwtDecoder, UserDetailsManager userDetailsManager){
    //     this.jwtDecoder=jwtDecoder;
    //     this.userDetailsManager=userDetailsManager;
    // }
    // public ResourceSConfig(JwtDecoder jwtDecoder, MyAuthenticationProvider myAuthenticationProvider, UserDetailsManager userDetailsManager){
    //     this.jwtDecoder=jwtDecoder;
    //     this.myAuthenticationProvider=myAuthenticationProvider;
    //     this.userDetailsManager=userDetailsManager;
    // }

     @Bean
    // @Order(2) //look at this 2 because i have spent approx 6 hours on debugging and this was the solution so appreciate it!
    @Order(10) 
    public SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.authorizeHttpRequests(
            auth->auth
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**")).authenticated()
                    .anyRequest().authenticated()
        );   

         http.oauth2ResourceServer(r->r.jwt(Customizer.withDefaults()));
        // http.oauth2ResourceServer(oauth2ResourceServer ->
        // oauth2ResourceServer
        //     .jwt(jwt ->
        //         jwt
        //             // .authenticationManager(new ProviderManager(myAuthenticationProvider))
        //             // .authenticationManager(new ProviderManager(new JwtAuthenticationProvider(jwtDecoder)))
        //             // .jwtAuthenticationConverter(jwtAuthenticationConverter())
        //     )
        // );
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingFilter());
        registrationBean.addUrlPatterns("/*"); // Apply filter to all URLs
        return registrationBean;
    }
    
    public void setAuthenticationProvider(MyAuthenticationProvider auth){
        this.myAuthenticationProvider=auth;
    }




}
