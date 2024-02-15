package hu.project.groupproject.authorizationserver.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class UserManagementConfig {
        @Autowired
        private DataSource dataSource;

        @Bean
        @Order(1)
        public SecurityFilterChain userManagementSecurityFilterChain(
                        HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(
                                authz -> authz

                                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
                                                .permitAll()
                                                .requestMatchers(AntPathRequestMatcher.antMatcher("/error/**"))
                                                .permitAll()
                                                .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**"))
                                                .permitAll()
                                                // .anyRequest().authenticated()
                                                .anyRequest().permitAll()
                )
                                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                                .csrf(csrf -> csrf
                                                // .ignoringRequestMatchers(
                                                //                 AntPathRequestMatcher.antMatcher("/h2-console/**"))
                                                // Apply CSRF protection only to /login/**
                                                .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/**"))
                                                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/login/**")) 
                                )
                                // TODO:set the form login to custom page
                                .formLogin(Customizer.withDefaults()).cors(Customizer.withDefaults())
                                .authenticationManager(new ProviderManager(DaoAuthenticationProvider()))
                                .passwordManagement(Customizer.withDefaults());
                return http.build();
        }


        // TODO:extend JdbcUserDetailsManager and migrate user details from the resource
        // server to here(email,phone,names...)
        // TODO:build a central authority/role database where users, clients, and in
        // this particular instance organisations(from the resource server orgs)
        // are all taken into account and a user can have "common roles" (USER, ADMIN,
        // AUDITOR...) and "org roles" (ORG_STAFF, ORG_AUDITOR, ORG_ADMIN...)
        // all grouped by the registered clients
        @Bean
        UserDetailsManager userDetailsManager() {
                
                UserDetails user = User.builder()
                                .username("user")
                                .password("{bcrypt}$2a$10$rwdYdhJR6lN0AKxOgAHtAeLkwO9qg1thF9NyQPSDeRAfSR3KG1j8y")
                                .roles("USER")
                                .build();
                UserDetails admin = User.builder()
                                .username("admin")
                                .password("{bcrypt}$2a$10$rwdYdhJR6lN0AKxOgAHtAeLkwO9qg1thF9NyQPSDeRAfSR3KG1j8y")
                                .roles("USER", "ADMIN")
                                .build();
                JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
                users.createUser(user);
                users.createUser(admin);
                
                return users;
        }

        @Bean 
        public AuthenticationProvider DaoAuthenticationProvider(){
                DaoAuthenticationProvider pw = new DaoAuthenticationProvider();
                pw.setPasswordEncoder(passwordEncoder());
                pw.setUserDetailsService(userDetailsManager());
                return pw;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        }
}
