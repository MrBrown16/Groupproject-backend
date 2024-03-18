package hu.project.groupproject.resourceserver.customAuth;

import org.springframework.security.test.context.support.*;

@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface WithMockCustomUser {
	String id();
	String username();
	String[] roles() default {"USER"};
}
