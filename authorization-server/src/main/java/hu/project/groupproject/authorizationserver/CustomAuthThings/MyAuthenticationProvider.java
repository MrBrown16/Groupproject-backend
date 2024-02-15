package hu.project.groupproject.authorizationserver.CustomAuthThings;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
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
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;



@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    private JwtDecoder jwtDecoder;
    UserDetailsService userDetailsService;
    PasswordEncoder passwordEncoder;

    JwtToPrincipalConverter jwtToPrincipalConverter = new JwtToPrincipalConverter();

    private final Log logger = LogFactory.getLog(getClass());
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter = new MyJwtAuthenticationConverter(
            this.jwtToPrincipalConverter);
    
    @Autowired //Don't trust it, And don't you touch it!! Without it the whole thing breaks!!
    public MyAuthenticationProvider(UserDetailsService userDetailsService,JwtDecoder jwtDecoder,PasswordEncoder passwordEncoder) {
        this.userDetailsService=userDetailsService;
        this.jwtDecoder=jwtDecoder;
        this.passwordEncoder=passwordEncoder;
    }
    // public MyAuthenticationProvider(JwtDecoder jwtDecoder,UserDetailsService userDetailsService,PasswordEncoder passwordEncoder) {
    //     Assert.notNull(jwtDecoder, "jwtDecoder cannot be null");
    //     Assert.notNull(userDetailsService, "userDetailsService cannot be null");
    //     this.jwtDecoder = jwtDecoder;
    //     // this.setUserDetailsService(userDetailsService);
    //     // this.setPasswordEncoder(passwordEncoder);
    // }
    //TODO:rewrite to authenticate both jwt and username password
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication auth = authentication;
        if( authentication.getClass()==BearerTokenAuthenticationToken.class){

            BearerTokenAuthenticationToken bearer = (BearerTokenAuthenticationToken) authentication;
            Jwt jwt = getJwt(bearer);
            AbstractAuthenticationToken token = this.jwtAuthenticationConverter.convert(jwt);
            if (token.getDetails() == null) {
                token.setDetails(bearer.getDetails());
            }
            this.logger.debug("Authenticated token");
            auth = token;
        }
        
        if (authentication.getClass()==UsernamePasswordAuthenticationToken.class) {
            auth = super.authenticate(authentication);
        }
        System.out.println("_______________________________"+auth+"___________________________________________");
        return auth;
    }

    private Jwt getJwt(BearerTokenAuthenticationToken bearer) {
        try {
            return this.jwtDecoder.decode(bearer.getToken());
        } catch (BadJwtException failed) {
            this.logger.debug("Failed to authenticate since the JWT was invalid");
            throw new InvalidBearerTokenException(failed.getMessage(), failed);
        } catch (JwtException failed) {
            throw new AuthenticationServiceException(failed.getMessage(), failed);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)) {
            return true;
        }else if (BearerTokenAuthenticationToken.class.isAssignableFrom(authentication)) {
            return true;
        }
        return false;
    }

    public void setJwtAuthenticationConverter(
            Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter) {
        Assert.notNull(jwtAuthenticationConverter, "jwtAuthenticationConverter cannot be null");
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }




    /**
     * Thank You https://stackoverflow.com/users/2938594/lance-fallon for this Answer: https://stackoverflow.com/a/45019672
     * it saved me hours at least!!
     * (I know they won't see this, but still)
     */
    @Override
    protected void doAfterPropertiesSet() {
        if(super.getUserDetailsService() != null){
            System.out.println("UserDetailsService has been configured properly");
        }
    }



}
