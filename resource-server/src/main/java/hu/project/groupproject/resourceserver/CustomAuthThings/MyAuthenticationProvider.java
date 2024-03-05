package hu.project.groupproject.resourceserver.CustomAuthThings;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import java.lang.System.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

// import hu.project.groupproject.authorizationserver.config.AuthorizationServerConfig;
// import hu.project.groupproject.authorizationserver.config.ResourceSConfig;
// import hu.project.groupproject.authorizationserver.config.UtilBeansThingy;
import jakarta.annotation.PostConstruct;

// @Component
public class MyAuthenticationProvider implements AuthenticationProvider {
    // @Autowired
    // private AuthorizationServerConfig authorizationServerConfig;
    // @Autowired
    // ResourceSConfig resourceSConfig;
    // @Autowired
    // UtilBeansThingy utilBeansThingy;
    // private JwtDecoder jwtDecoder;
    // UserDetailsService userDetailsService;
    // PasswordEncoder passwordEncoder;
    // JwtAuthenticationProvider
    // JwtToPrincipalConverter jwtToPrincipalConverter = new JwtToPrincipalConverter();

    private final Log logger = LogFactory.getLog(getClass());
    

    // private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter = new MyJwtAuthenticationConverter(
    //         this.jwtToPrincipalConverter);
    
    // @Autowired //Don't trust it, And don't you touch it!! Without it the whole thing breaks!!
    // public MyAuthenticationProvider(JwtDecoder jwtDecoder,PasswordEncoder passwordEncoder,UserDetailsService userDetailsService) {
    //     this.userDetailsService=userDetailsService;
    //     this.jwtDecoder=jwtDecoder;
    //     this.passwordEncoder=passwordEncoder;
    // }
    // public MyAuthenticationProvider(JwtDecoder jwtDecoder,UserDetailsService userDetailsService,PasswordEncoder passwordEncoder) {
    //     Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
    //     Assert.notNull(userDetailsService, "userDetailsService cannot be null");
    //     this.jwtDecoder = jwtDecoder;
    //     // this.setUserDetailsService(userDetailsService);
    //     // this.setPasswordEncoder(passwordEncoder);
    // }
    //because the supported authentication can only be created through previous validation and authorization 
    //it returns the parameter unchanged
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Authentication auth = authentication;
        this.logger.debug("MyAuthenticationProvider.authenticate() Authentication: "+authentication+"  authentication.getClass():  "+authentication.getClass());
        return authentication;
        //This whole thing is unnecessary
        // if( authentication.getClass()==MyJwtAuthenticationToken.class){
        //     logger.debug("MyJwtAuthenticationToken");
        //     MyJwtAuthenticationToken myToken = (MyJwtAuthenticationToken) authentication;
        //     Jwt jwt = (Jwt) myToken.getToken();
        //     AbstractAuthenticationToken token = this.jwtAuthenticationConverter.convert(jwt);
        //     if (token.getDetails() == null) {
        //         token.setDetails(myToken.getDetails());
        //     }
        //     this.logger.debug("Authenticated token");
        //     auth = token;
        // }
        
        // if (authentication.getClass()==UsernamePasswordAuthenticationToken.class) {
        //     logger.debug("UsernamePasswordAuthenticationToken");
        //     auth = super.authenticate(authentication);
        // }
        // System.out.println("IsDebugEnabled:  "+logger.isDebugEnabled()+"  auth:  "+auth);
        // this.logger.debug("MyAuthenticationProvider.authenticate() END auth.isAuthenticated()  "+auth.isAuthenticated());
        // return auth;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return MyJwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    // public void setJwtAuthenticationConverter(
    //         Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter) {
    //     Assert.notNull(jwtAuthenticationConverter, "jwtAuthenticationConverter cannot be null");
    //     this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    // }






    /**
     * Thank You https://stackoverflow.com/users/2938594/lance-fallon for this Answer: https://stackoverflow.com/a/45019672
     * it saved me hours at least!!
     * (I know they won't see this, but still)
     */
    // @Override
    // protected void doAfterPropertiesSet() {
    //     if(super.getUserDetailsService() != null){
    //         System.out.println("UserDetailsService has been configured properly");
    //     }
    // }

    // @PostConstruct
    // private void setAuthenticationProviderDependencies(){
    //     authorizationServerConfig.setAuthenticationProvider(this);
    //     resourceSConfig.setAuthenticationProvider(this);
    //     utilBeansThingy.setUserDetailsService(this);
    // }

    
    // @Override
    // public void setProperties(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtDecoder jwtDecoder) {
    //     if (userDetailsService!=null && passwordEncoder!=null) {
    //         setProperties(passwordEncoder,userDetailsService);
    //         this.userDetailsService=userDetailsService;
    //         this.passwordEncoder=passwordEncoder;
    //     }
    //     this.jwtDecoder=jwtDecoder;
    // }



}
