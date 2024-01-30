package hu.project.groupproject.authorizationserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomTestController {
    @GetMapping("/login")
    public void testM(){
        
    }
}
