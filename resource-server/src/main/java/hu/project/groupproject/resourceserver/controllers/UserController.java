package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNew;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @PersistenceContext
    EntityManager manager;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping
    public ImageUploadDetailsDto newUser(@RequestBody UserDtoNew user){
        Optional<MyUser> myUser = userService.newUser(user);
        String url= myUser.get().getPath();
        return new ImageUploadDetailsDto(url, false);
    }
    @PutMapping("/{id}")
    public ImageUploadDetailsDto updateUser(@PathVariable("id") String id, @RequestBody UserDtoNew user){
        Optional<MyUser> myUser = userService.updateUser(id, user);
        String url= myUser.get().getPath();
        return new ImageUploadDetailsDto(url, false);
    }


    @GetMapping("/{id}")
    public Optional<UserDtoPublic> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }
    @GetMapping("/{userName}")
    public Optional<UserDtoPublic> getUserByName(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }
    
    @DeleteMapping("/del/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
    
}
