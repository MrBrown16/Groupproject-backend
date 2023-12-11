package hu.project.groupproject.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/")
public class MainController {
    
    @GetMapping
    public void setUp(){
        //TODO:populate database
    }
}
