package hu.project.groupproject.authorizationserver.CustomAuthThings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class JwtToPrincipalConverter implements Converter<Jwt, User> {

    @Autowired
    JdbcUserDetailsManager manager;



    @Override
    @Nullable
    public User convert(Jwt source) {
        String sub = source.getSubject();
        User user = (User) manager.loadUserByUsername(sub);
        System.out.println("_____________________________________ "+user.toString()+" _________________________________________");
        // user.getAuthorities();
        return user;
    }
    
}
