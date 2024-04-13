package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.NoticeDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.ReservationDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.UserInfoDto;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublicPartial;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicExtended;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNew;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNewWithPW;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.NoticeService;
import hu.project.groupproject.resourceserver.services.OrgService;
import hu.project.groupproject.resourceserver.services.PostService;
import hu.project.groupproject.resourceserver.services.ReservationService;
import hu.project.groupproject.resourceserver.services.RoleService;
import hu.project.groupproject.resourceserver.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;
import java.util.Set;
import java.rmi.UnexpectedException;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/user") //hasrole('USER') is set in SecurityConfig
public class UserController {

    protected final Log logger = LogFactory.getLog(getClass());


    UserService userService;
    PostService postService;
    OrgService orgService;
    NoticeService noticeService;
    ReservationService reservationService;
    RoleService roleService;

    @PersistenceContext
    EntityManager manager;
    
    public UserController(UserService userService, PostService postService, NoticeService noticeService, ReservationService reservationService,OrgService orgService, RoleService roleService) {
        this.userService=userService;
        this.postService=postService;
        this.noticeService = noticeService;
        this.reservationService = reservationService;
        this.orgService=orgService;
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public Optional<UserDtoPublic> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }
    @GetMapping("/{userName}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public Optional<UserDtoPublic> getUserByName(@PathVariable String userName) {
        return userService.getUserByUserName(userName);
    }
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public Page<UserDtoPublicPartial> searchPubUsersByProperty(@RequestParam("value") String value, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
        return userService.getUsersByPropertyLikePublic(pageNum, value, category);
    }
    @GetMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDtoPrivatePartial> searchPrivateUsersByProperty(@RequestParam("value") String value, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
        return userService.getUsersByPropertyLikePrivate(pageNum, value, category);
    }
    
    @GetMapping("/myUserInfo")
    // @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public UserInfoDto getUserInfo(Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        Set<String> orgIds = new HashSet<String>();
        orgIds = userService.getOrgsIdsForUser(user.getId());
        Set<String> roles = new HashSet<String>();
        auth.getAuthorities().forEach(i->roles.add(i.getAuthority()));
        return new UserInfoDto(user.getId(), user.getEmail(), user.getUserName(), user.getPhone(), roles, "", orgIds);
    }
    
    @GetMapping("/{userId}/posts")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public Set<PostDtoPublicExtended> getPostsForUser(@PathVariable String userId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null && user.getId() == userId) {
            return postService.getPostsForUser(userId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/{userId}/orgs")
    // @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public Set<OrgDtoPublicPartial> getOrgsForUser(@PathVariable String userId, @RequestParam("pageNum") int pageNum, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        // if (user != null && user.getId() == userId) {
            return orgService.getOrgsByUserId(userId,pageNum);
        // }
        // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/{userId}/notices")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public Set<NoticeDtoPublic> getNoticesForUser(@PathVariable String userId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null && user.getId() == userId) {
            return noticeService.getNoticesForUser(userId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/{userId}/reservations")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public Set<ReservationDtoPublic> getReservationsForUser(@PathVariable String userId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null && user.getId() == userId) {
            return reservationService.getReservationsForUser(userId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    
    
    @PostMapping
    public ImageUploadDetailsDto newUser(@RequestBody UserDtoNewWithPW user) throws UnexpectedException{
        logger.debug("register......");
        return userService.newUser(user);
    }
    
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public ImageUploadDetailsDto updateUser(@PathVariable("id") String id, @RequestBody UserDtoNew user){
        return userService.updateUser(id, user);
    }
    
    
    @DeleteMapping("/del/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    //doesn't work
    @GetMapping("logout")
    public void logout(Authentication auth){
        // return auth.getCredentials();
        
        userService.logout(auth);
    }

    //TODO: role management 


}
