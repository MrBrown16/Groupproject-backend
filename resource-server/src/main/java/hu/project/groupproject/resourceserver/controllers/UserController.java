package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.NoticeDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.ReservationDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.UserInfoDto;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicExtended;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNew;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNewWithPW;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.NoticeService;
import hu.project.groupproject.resourceserver.services.PostService;
import hu.project.groupproject.resourceserver.services.ReservationService;
import hu.project.groupproject.resourceserver.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;
import java.util.Set;
import java.rmi.UnexpectedException;
import java.util.HashSet;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;




@RestController
@RequestMapping("/user") //hasrole('USER') is set in SecurityConfig
public class UserController {

    UserService userService;
    PostService postService;
    NoticeService noticeService;
    ReservationService reservationService;

    @PersistenceContext
    EntityManager manager;
    
    public UserController(UserService userService, PostService postService, NoticeService noticeService, ReservationService reservationService){
        this.userService=userService;
        this.postService=postService;
        this.noticeService = noticeService;
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public Optional<UserDtoPublic> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }
    @GetMapping("/{userName}")
    public Optional<UserDtoPublic> getUserByName(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }
    
    @GetMapping("/myUserInfo")
    public UserInfoDto getUserInfo(Authentication authentication) {
        MyUser user = (MyUser)authentication.getPrincipal();
        Set<String> orgIds = new HashSet<String>();
        orgIds = userService.getOrgsIdsForUser(user.getId());
        Set<String> roles = new HashSet<String>();
        authentication.getAuthorities().forEach(i->roles.add(i.getAuthority()));
        return new UserInfoDto(user.getId(), user.getEmail(), user.getUserName(), user.getPhone(), roles, "", orgIds);
    }
    
    @GetMapping("/{userId}/posts")
    public Set<PostDtoPublicExtended> getPostsForUser(@PathVariable String userId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null && user.getId() == userId) {
            return postService.getPostsForUser(userId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/{userId}/notices")
    public Set<NoticeDtoPublic> getNoticesForUser(@PathVariable String userId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null && user.getId() == userId) {
            return noticeService.getNoticesForUser(userId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/{userId}/reservations")
    public Set<ReservationDtoPublic> getReservationsForUser(@PathVariable String userId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null && user.getId() == userId) {
            return reservationService.getReservationsForUser(userId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    
    @PostMapping
    public ImageUploadDetailsDto newUser(@RequestBody UserDtoNewWithPW user) throws UnexpectedException{
        return userService.newUser(user);
    }


    @PutMapping("/{id}")
    public ImageUploadDetailsDto updateUser(@PathVariable("id") String id, @RequestBody UserDtoNew user){
        return userService.updateUser(id, user);
    }


    @DeleteMapping("/del/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

}
