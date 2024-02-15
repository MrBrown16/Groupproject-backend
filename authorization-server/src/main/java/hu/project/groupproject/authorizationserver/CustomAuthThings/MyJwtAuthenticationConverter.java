package hu.project.groupproject.authorizationserver.CustomAuthThings;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.Assert;


public class MyJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    
    private final Converter<Jwt, User> jwtToPrincipalConverter;
    
    private String principalClaimName = JwtClaimNames.SUB;

    
    public MyJwtAuthenticationConverter(
        Converter<Jwt, User> jwtToPrincipalConverter,
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
            
            this(jwtToPrincipalConverter);
            setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    }
	public MyJwtAuthenticationConverter(Converter<Jwt, User> jwtToPrincipalConverter) {
		Assert.notNull(jwtToPrincipalConverter, "jwtToPrincipalConverter cannot be null");
		this.jwtToPrincipalConverter = jwtToPrincipalConverter;
	}
    //ORIGINAL    
    // public JwtAuthenticationConverter(Converter<Jwt, User> jwtToPrincipalConverter) {
    // 	Assert.notNull(jwtToPrincipalConverter, "jwtToPrincipalConverter cannot be null");
    // 	this.jwtToPrincipalConverter = jwtToPrincipalConverter;
    // }


    // @Override
    // public final AbstractAuthenticationToken convert(Jwt jwt) {
    //     Collection<GrantedAuthority> authorities = this.jwtGrantedAuthoritiesConverter.convert(jwt);
        
    //     String principalClaimValue = jwt.getClaimAsString(this.principalClaimName);
    //     return new MyJwtAuthenticationToken(jwt, authorities, principalClaimValue);
    // }
    // TODO:rewrite for custom principal class 
    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) {
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

    
}
