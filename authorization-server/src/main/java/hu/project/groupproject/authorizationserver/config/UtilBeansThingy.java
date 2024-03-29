package hu.project.groupproject.authorizationserver.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class UtilBeansThingy {
        @Autowired
        DataSource dataSource;

        
        // TODO:build a central authority/role database in auth server where users, clients, and in
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
                UserDetails orgAdmin = User.builder()
                                .username("orgadmin")
                                .password("{bcrypt}$2a$10$rwdYdhJR6lN0AKxOgAHtAeLkwO9qg1thF9NyQPSDeRAfSR3KG1j8y")
                                .roles("USER", "ORG_ADMIN")
                                .build();
                UserDetails adminOrgAdmin = User.builder()
                                .username("adminorgadmin")
                                .password("{bcrypt}$2a$10$rwdYdhJR6lN0AKxOgAHtAeLkwO9qg1thF9NyQPSDeRAfSR3KG1j8y")
                                .roles("USER", "ADMIN","ORG_ADMIN")
                                .build();
                JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
                users.createUser(user);
                users.createUser(admin);
                users.createUser(orgAdmin);
                users.createUser(adminOrgAdmin);
                return users;
        }


        @Bean
        public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
                return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
        }


        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        }

}
