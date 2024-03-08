package hu.project.groupproject.resourceserver.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.services.UserService;


@RestController
public class DemoController {

    protected final Log logger = LogFactory.getLog(getClass());
    

    @Autowired
    UserService userService;


    // GET localhost:8082/hello
    @GetMapping("hello")
    // @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String getHello(
        Authentication authentication
        ) {
            // String username = authentication.getName();
            return "Authentication:  "+authentication.toString()+" Principal: "+authentication.getPrincipal();
            // return Collections.singletonMap("get_text", "Hello " + username);
        }
        
    @PostMapping("hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String postHello(
            Authentication authentication,
            @RequestBody String ize
    ) {
        String username = authentication.getName();
        return "post_text Hello " + username + authentication+ ize;
    }
    //     @PostMapping("hello")
    //     @PreAuthorize("hasRole('ADMIN')")
    // public Map<String, String> postHello(
    //         Authentication authentication,
    //         @RequestBody String ize
    // ) {
    //     String username = authentication.getName();
    //     return Collections.singletonMap("post_text", "Hello " + username + authentication+ ize);
    // }
    @GetMapping("orgIds/{id}")
    public Set<String> getOrgsForUserInfo(@PathVariable("id") String id){
        Set<String> orgIds = new HashSet<String>();

        orgIds = userService.getOrgsIdsForUser(id);
        logger.debug("OrgIds for userInfo: "+orgIds);
        return orgIds;
    }

}
