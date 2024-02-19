package hu.project.groupproject.authorizationserver.CustomAuthThings;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.Assert;


public class MyJwtAuthenticationConverter implements Converter<Jwt, MyJwtAuthenticationToken> {
    
    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private JwtTimestampValidator timeValidator = new JwtTimestampValidator();
	private JwtIssuerValidator issvalidator = new JwtIssuerValidator("http://localhost:8083");
    private final JwtToPrincipalConverter jwtToPrincipalConverter = new JwtToPrincipalConverter();
    
    private String principalClaimName = JwtClaimNames.SUB;

    private final Log logger = LogFactory.getLog(getClass());

    public MyJwtAuthenticationConverter(){}
    
    public MyJwtAuthenticationConverter(
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
            
            setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    }
	// public MyJwtAuthenticationConverter(JwtToPrincipalConverter jwtToPrincipalConverter) {
	// 	Assert.notNull(jwtToPrincipalConverter, "jwtToPrincipalConverter cannot be null");
	// 	this.jwtToPrincipalConverter = jwtToPrincipalConverter;
	// }

    // TODO:rewrite for custom principal class 
    @Override
    public final MyJwtAuthenticationToken convert(@NonNull Jwt jwt) throws AuthenticationException{
        if (!validate(jwt)) {
            throw new BadCredentialsException(OAuth2ErrorCodes.INVALID_TOKEN);
        }
        logger.debug("MyJwtAuthenticationConverter.convert() jwt:  "+jwt);
        User principal = jwtToPrincipalConverter.convert(jwt);
        Collection<GrantedAuthority> jwtAuthorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        Collection<GrantedAuthority> authorities = Stream.of(jwtAuthorities, principal.getAuthorities())
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toUnmodifiableList());

        return new MyJwtAuthenticationToken(jwt, principal, authorities);
    }


    public void setJwtGrantedAuthoritiesConverter(
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
    }

    public void setPrincipalClaimName(String principalClaimName) {
        Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
        this.principalClaimName = principalClaimName;
    }

    private boolean validate(Jwt jwt){
        if (issvalidator.validate(jwt)==OAuth2TokenValidatorResult.success()) {
            if (timeValidator.validate(jwt)==OAuth2TokenValidatorResult.success()) {
                return true;
            }
        }
        return false;
    }
    
}
