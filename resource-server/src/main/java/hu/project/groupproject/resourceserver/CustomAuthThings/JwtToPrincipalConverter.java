package hu.project.groupproject.resourceserver.CustomAuthThings;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;

import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class JwtToPrincipalConverter implements Converter<Jwt, MyUser> {

    @PersistenceContext
    EntityManager manager;

    UserService userService;

    public JwtToPrincipalConverter(){
        
    }
    public void setUserService(UserService userService){
        this.userService=userService;
    }
    public void setManager(EntityManager manager){
        this.manager=manager;
    }

    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    @Nullable
    public MyUser convert(@NonNull Jwt source) {
        String userName = source.getSubject();
        Optional<UserDtoPublic> preUser = userService.getUserByUserName(userName);
        if (preUser.isPresent()) {
            MyUser user = manager.find(MyUser.class, preUser.get().id());
            if (user != null) {
                logger.debug("User not Null user: "+ user.toString()+" Username: "+ userName);
                return user;
            }
            logger.debug("User Null user: "+ user+" Username: "+ userName);
        }
        logger.debug("User Null user: "+userName);
        throw new UsernameNotFoundException("There is no corresponding entity in this application");
    }
    
}
