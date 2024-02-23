package hu.project.groupproject.authorizationserver.config;

import java.time.Duration;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.t;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2ClientAuthenticationConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import hu.project.groupproject.authorizationserver.CustomAuthThings.MyAuthenticationProvider;
import hu.project.groupproject.authorizationserver.CustomAuthThings.MyJwtAuthenticationConverter;
import hu.project.groupproject.authorizationserver.CustomAuthThings.MyJwtAuthenticationToken;
import hu.project.groupproject.authorizationserver.CustomAuthThings.MyUserDetailsAuthenticationProvider;
import hu.project.groupproject.authorizationserver.filters.LoggingFilter;



@Configuration
public class AuthorizationServerConfig {
    @Autowired
    DataSource dataSource;

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    UtilBeansThingy utilBeansThingy;


    // // @Autowired
    // MyAuthenticationProvider myAuthenticationProvider;

    // public void setAuthenticationProvider(MyAuthenticationProvider auth){
    //     this.myAuthenticationProvider=auth;
    //     this.logger.debug("#############AuthorizationServerConfig.myAuthenticationProvider##################### auth: "+auth+"  #####################################");
    // }



    // TODO: register security rules/filters in the correst order something like
    // this:
    // (https://github.com/spring-projects/spring-security/blob/6.2.1/config/src/main/java/org/springframework/security/config/annotation/web/builders/FilterOrderRegistration.java)
    @Bean
    @Order(5)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {
// OAuth2AuthorizationCodeAuthenticationProvider
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        // OAuth2ClientAuthenticationConfigurer authServerConf = new OAuth2ClientAuthenticationConfigurer();
        // authServerConf.authenticationProvider(myAuthenticationProvider)
                    // ;
        // .authorizationEndpoint(
        //     authE->authE
        //             .authenticationProvider(myAuthenticationProvider)
                    
        // ).clientAuthentication(a->a.authenticationProvider(myAuthenticationProvider));
        // http.apply(authServerConf);
        // http.securityContext(c->c.disable());
        
        // http.authorizeHttpRequests(
        //     auth->auth
                    
        //             .anyRequest().authenticated()
        //     );
        // http.oauth2Login(c->c.);
        http.anonymous(a->a.disable());
        http
		.securityContext((securityContext) -> securityContext
			.securityContextRepository(new DelegatingSecurityContextRepository(
				new RequestAttributeSecurityContextRepository(),
				new HttpSessionSecurityContextRepository()
			))
		);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults())
                
                // .authorizationEndpoint(e->e
                //         .authenticationProvider(myAuthenticationProvider)
                        
                // )
                // .clientAuthentication(c->c.authenticationProvider(myAuthenticationProvider))
                
                ;

        http.exceptionHandling(c -> c
                // .defaultAuthenticationEntryPointFor(null, null)
                .defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
        http.sessionManagement(session -> {
            session
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER);
        });
        // http.authenticationProvider(myAuthenticationProvider);
        return http.build();
    }



    @Bean
    @Order(6)
    public SecurityFilterChain userManagementSecurityFilterChain(
            HttpSecurity http) throws Exception {
        // http.authorizeHttpRequests(
        //         authz -> authz

        //                 // .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
        //                 // .permitAll()
        //                 // .requestMatchers(AntPathRequestMatcher.antMatcher("/.well-known/**"))
        //                 // .permitAll()
        //                 // .requestMatchers(AntPathRequestMatcher.antMatcher("/error/**"))
        //                 // .permitAll()
        //                 // .requestMatchers(AntPathRequestMatcher.antMatcher("/user/**"))
        //                 // .permitAll()
        //                 .anyRequest().authenticated()
        //                 // .anyRequest().permitAll()
        //                 )
            http
                .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable())
                .csrf(csrf -> csrf
                        // .ignoringRequestMatchers(
                        // AntPathRequestMatcher.antMatcher("/h2-console/**"))
                        // Apply CSRF protection only to /login/**
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/**"))
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/login/**")))
                // .securityContext(c->c.disable())
                
                // TODO:set the form login to custom page
                .formLogin(Customizer.withDefaults()).cors(Customizer.withDefaults())
                // .authenticationProvider(myAuthenticationProvider)
                .authenticationManager(providerManager())
                // .passwordManagement(Customizer.withDefaults())
                ;
        return http.build();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .build();
    }
    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LoggingFilter());
        registrationBean.addUrlPatterns("/*"); 
        return registrationBean;
    }
    
    @Bean
    public ProviderManager providerManager(){
        return new ProviderManager(new MyAuthenticationProvider(),new MyUserDetailsAuthenticationProvider(utilBeansThingy.passwordEncoder(), utilBeansThingy.userDetailsManager()));
    }

    @Bean
    public Converter<Jwt, MyJwtAuthenticationToken> authenticationConverter(){
        return new MyJwtAuthenticationConverter();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedOrigin("http://localhost:8082");
        config.addAllowedOrigin("http://localhost:8083");
        config.addAllowedMethod("*"); // Allow all HTTP methods
        config.addAllowedHeader("*"); // Allow all headers
        config.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)
        source.registerCorsConfiguration("/**", config); // Apply CORS to all paths
        
        return new CorsFilter(source);
    }
    
    // TODO:possible improvement (priority:low): change in memory clientRepo to
    // jdbc/JPA in database
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient client = RegisteredClient.withId("1")
        .clientId("myClient")
        .clientSecret("{bcrypt}$2a$10$Veul2yB.g3A4hmtHbmnQeuwfJDrYSfwQ8nilh0f8d5f6tM1mGPPQ.")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
        .scope(OidcScopes.PROFILE)
        .scope(OidcScopes.OPENID)
        .redirectUri("http://localhost:8081/login/oauth2/code/myClient")
        .postLogoutRedirectUri("http://localhost:8081")
        .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofSeconds(3600L)).build())
        .build();
        
        return new InMemoryRegisteredClientRepository(client);
    }
    
    //TODO:add authorities instead of scopes
    // @Bean
    // public OAuth2TokenCustomizer<JwtEncodingContext> auth2TokenCustomizer(){
    //     return context -> {
    //         context.getClaims().claim("authorities", context.getAuthorizedScopes());
    //     };
    // }



}