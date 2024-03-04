package hu.project.groupproject.resourceserver.CustomAuthThings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.provisioning.JdbcUserDetailsManager;


public class JwtToPrincipalConverter implements Converter<Jwt, User> {

    @Autowired
    JdbcUserDetailsManager manager;

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    @Nullable
    public User convert(@NonNull Jwt source) {
        String sub = source.getSubject();
        User user = (User) manager.loadUserByUsername(sub);
        this.logger.debug("_____________________________________ "+user.toString()+" _________________________________________");
        // user.getAuthorities();
        return user;
        
        // return new User("","",null);
    }
    
}
