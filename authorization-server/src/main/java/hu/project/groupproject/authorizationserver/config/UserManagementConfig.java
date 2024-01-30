package hu.project.groupproject.authorizationserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class UserManagementConfig {

    //TODO:set public parts to public
    @Bean
    @Order(1)
    public SecurityFilterChain userManagementSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.authorizeHttpRequests(
                authz -> authz
                        .anyRequest().authenticated()
        )
        //TODO:set the form login to custom page
                .formLogin(Customizer.withDefaults()).cors(Customizer.withDefaults());
                // .formLogin(Customizer.withDefaults()).cors(c->c.disable());
        return http.build();
    }

    //TODO:Change in memory to jdbc in database and create accessors
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("pass")
                .authorities("read")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    //TODO:Create correct password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
