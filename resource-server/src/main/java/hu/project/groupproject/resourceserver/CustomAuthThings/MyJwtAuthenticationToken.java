package hu.project.groupproject.resourceserver.CustomAuthThings;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

@Transient
public class MyJwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String name;

    public MyJwtAuthenticationToken(Jwt jwt) {
        super(jwt);
        this.name = jwt.getSubject();
    }

    public MyJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
        this.setAuthenticated(true);
        this.name = jwt.getSubject();
    }
    //Custom
    public MyJwtAuthenticationToken(Jwt jwt, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, principal, jwt, authorities);
        this.setAuthenticated(true);
        this.name = jwt.getSubject();
    }

    public MyJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities);
        this.setAuthenticated(true);
        this.name = name;
    }

    @Override
    public Map<String, Object> getTokenAttributes() {
        return this.getToken().getClaims();
    }

    @Override
    public String getName() {
        return this.name;
    }


}
