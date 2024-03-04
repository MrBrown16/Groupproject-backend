package hu.project.groupproject.resourceserver.CustomAuthThings;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;

public interface SetAuthProviderProperties {

    public void setProperties(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtDecoder jwtDecoder);
}
