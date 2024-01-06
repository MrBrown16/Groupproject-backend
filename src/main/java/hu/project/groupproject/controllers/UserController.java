package hu.project.groupproject.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.dtos.userDTOs.UserDTOPublic;
import hu.project.groupproject.entities.MyUser;
import hu.project.groupproject.services.UserService;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping
    public MyUser saveUser(@RequestBody MyUser user){
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public Optional<UserDTOPublic> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
    
    @PostMapping("/del")
    public void deleteUser(@RequestBody MyUser user) {
        userService.deleteUser(user);
    }
    
}
