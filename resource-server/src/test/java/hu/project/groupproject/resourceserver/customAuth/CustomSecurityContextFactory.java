package hu.project.groupproject.resourceserver.customAuth;

import hu.project.groupproject.resourceserver.CustomAuthThings.*;
import hu.project.groupproject.resourceserver.entities.softdeletable.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.test.context.support.*;

import java.time.*;
import java.time.temporal.*;
import java.util.*;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser>{
	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser customUser){
		MyUser principal = new MyUser();
		
		principal.setId(customUser.id());
		principal.setUserName(customUser.username());
		
		
		Authentication authentication = new MyJwtAuthenticationToken(new Jwt("", Instant.now().minusSeconds(10),Instant.now().plus(10,ChronoUnit.MINUTES ),Collections.emptyMap(),Collections.emptyMap()),
				principal, Arrays.stream(customUser.roles()).map(SimpleGrantedAuthority::new).toList());
		authentication.setAuthenticated(true);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	
	}
}
