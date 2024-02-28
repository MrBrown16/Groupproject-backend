package hu.project.groupproject.resourceserver.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@RestController
public class DemoController {

    @Autowired
    EntityManager manager;
    
    // GET localhost:8082/hello
    @GetMapping("hello")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Map<String, String> getHello(
        Authentication authentication
        ) {
            String username = authentication.getName();
            return Collections.singletonMap("get_text", "Hello " + username);
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

    @PostMapping("img")
    @Transactional
    public String postMethodName(@RequestParam("images") MultipartFile[] images) throws IOException {
        MyUser user = new MyUser();
        manager.persist(user);
        user.saveImages(images);
        return "success i guess";
    }
    
}
