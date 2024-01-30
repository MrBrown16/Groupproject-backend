package hu.project.groupproject.resourceserver.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    
    // GET localhost:8082/hello
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("hello")
    public Map<String, String> getHello(
            Authentication authentication
    ) {
        String username = authentication.getName();
        return Collections.singletonMap("get_text", "Hello " + username);
    }

    @PostMapping("hello")
    public Map<String, String> postHello(
            Authentication authentication
    ) {
        String username = authentication.getName();
        return Collections.singletonMap("post_text", "Hello " + username);
    }
}
