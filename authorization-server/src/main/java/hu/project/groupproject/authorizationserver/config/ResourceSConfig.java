package hu.project.groupproject.authorizationserver.config;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import hu.project.groupproject.authorizationserver.CustomAuthThings.MyAuthenticationProvider;

// @Configuration
// @PropertySource("classpath:application.properties")
// @Order(80)
public class ResourceSConfig {
    // @Value("${myVariables.jwk-set-uri}")
    String jwkSetUri;
    // @Autowired
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

    //  @Bean
    // @Order(2) //look at this 2 because i have spent approx 6 hours on debugging and this was the solution so appreciate it!
    // @Order(10) 
    public SecurityFilterChain resourceServerSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.authorizeHttpRequests(
            auth->auth
                    .requestMatchers("user/**").authenticated()
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

    
    
    public void setAuthenticationProvider(MyAuthenticationProvider auth){
        this.myAuthenticationProvider=auth;
    }




}
