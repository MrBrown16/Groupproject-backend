package hu.project.groupproject.authorizationserver.config;

import javax.sql.DataSource;

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
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated()
                        )
                        .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                        .csrf(csrf -> csrf
                                .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                    )
        //TODO:set the form login to custom page
                .formLogin(Customizer.withDefaults()).cors(Customizer.withDefaults());
                // .formLogin(Customizer.withDefaults()).cors(c->c.disable());
        return http.build();
    }

    //TODO:Change in memory to jdbc in database and create accessors
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails user = User.withUsername("user")
    //             .password("pass")
    //             .authorities("read")
    //             .build();

    //     return new InMemoryUserDetailsManager(user);
    // }
    @Bean
    UserDetailsManager users(DataSource dataSource) {
        UserDetails user = User.builder()
                .username("user")
                .password("pass")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("pass")
                .roles("USER", "ADMIN")
                .build();
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.createUser(user);
        users.createUser(admin);
        return users;
    }

    //TODO:Create correct password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
