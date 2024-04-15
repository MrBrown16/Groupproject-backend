package hu.project.groupproject.resourceserver.services;

import java.rmi.UnexpectedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNew;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNewWithPW;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.enums.UserFields;
import hu.project.groupproject.resourceserver.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Service
public class UserService {
    
    protected final Log logger = LogFactory.getLog(getClass());

    UserRepository userRepository;
    RestClient restClient;

    @PersistenceContext
    EntityManager manager;
    
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
        this.restClient = RestClient.builder().baseUrl("http://localhost:8083/").build();
        
    }
   
    public MyUser saveUser(MyUser myUser){
        return userRepository.save(myUser);
    }
    public Optional<UserDtoPublic> getUser(String id){
        MyUser user = manager.find(MyUser.class, id);
        if (user != null) {
            return Optional.of(mapMyUserToUserDtoPublic(user));
        }
        return Optional.empty();
        // return userRepository.findById(id, UserDtoPublic.class);
    }

    public Optional<UserDtoPrivatePartial> getUserExtended(String id){
        MyUser user = manager.find(MyUser.class, id);
        if (user != null) {
            return Optional.of(mapMyUserToUserDtoPrivate(user));
        }
        return Optional.empty();
    }


    public Optional<UserDtoPublic> getUserByUserName(String username){
        Optional<UserDtoPublicPartial> optDtoPartial = userRepository.findByUserName(username);
        if (optDtoPartial.isPresent()) {
            UserDtoPublicPartial part = optDtoPartial.get();
            MyUser user = manager.find(MyUser.class, part.id());
            if (user!=null) {
                String profileImage = user.getPath();
                UserDtoPublic pub = new UserDtoPublic(part.id(), part.userName(), part.firstName(), part.lastName(), profileImage);
                return Optional.of(pub);
            }
        }
        return Optional.empty();
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    @Transactional
    public ImageUploadDetailsDto newUser(UserDtoNewWithPW newUser) throws UnexpectedException{
        logger.debug("newUser inside method");
        if (newUser.password1() != null && newUser.password2() != null && newUser.password1().equals(newUser.password2()) && newUser.phone() != null && newUser.email() != null && newUser.userName() != null) {
            logger.debug("newUser inside if");
            MyUser user = new MyUser();
            user.setEmail(newUser.email());
            user.setPhone(newUser.phone());
            user.setUserName(newUser.userName());
            if (newUser.firstName() != null && newUser.firstName().length() > 2) {
                user.setFirstName(newUser.firstName());
            }
            if (newUser.lastName() != null && newUser.lastName().length() > 2) {
                user.setLastName(newUser.lastName());
            }
            user = userRepository.save(user);
            //TODO: check if it works
            Map<String, String> userMap = new HashMap();
            userMap.put("userName", newUser.userName());
            userMap.put("password", newUser.password1());
            // UserDetails userDetails = User.builder().username(user.getUserName()).roles("USER").password(newUser.password1()).build();
            Boolean success = restClient.post().uri("/user/newUser").body(userMap).retrieve().body(Boolean.class);
            if (success == null || success == false) {
                throw new UnexpectedException("User login creation failed");
            }
            //TODO:save user in auth server (webClient http request to createNewUser) 
            String url= user.getPath();
            return new ImageUploadDetailsDto(url, false);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void logout(Authentication auth){
        
        restClient.post().uri("/connect/logout").body(Collections.singletonMap("token", ((Jwt) auth.getCredentials()).getTokenValue()));

    }

    @Transactional
    public ImageUploadDetailsDto updateUser(String id, UserDtoNew newUser){
        if (newUser.phone() != null && newUser.email() != null && newUser.userName() != null) {
            MyUser user = manager.find(MyUser.class, id);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            user.setEmail(newUser.email());
            user.setPhone(newUser.phone());
            user.setUserName(newUser.userName());
            if (newUser.firstName() != null && newUser.firstName().length() > 2) {
                user.setFirstName(newUser.firstName());
            }
            if (newUser.lastName() != null && newUser.lastName().length() > 2) {
                user.setLastName(newUser.lastName());
            }
            user = userRepository.save(user);
            String url= user.getPath();
            return new ImageUploadDetailsDto(url, false);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public Set<UserDtoPublic> getUsersForOrg(String orgId){
        MyOrg org = manager.find(MyOrg.class, orgId);
        Set<UserDtoPublic> users = new HashSet();
        org.getUsers().forEach(
            (e)-> users.add(mapMyUserToUserDtoPublic(e))
            );
        return users;
    }


    public Set<String> getOrgsIdsForUser(String id){
        Set<String> orgIds = userRepository.findOrgIdsByUserId(id);
        logger.debug(orgIds);
        return orgIds;
    }
    public Set<String> getPostsIdsForUser(String id){
        Set<String> postIds = userRepository.findPostIdsByUserId(id);
        logger.debug(postIds);
        return postIds;
    }
    public Set<String> getNewsIdsForUser(String id){
        Set<String> newsIds = userRepository.findNewsIdsByUserId(id);
        logger.debug(newsIds);
        return newsIds;
    }
    public Set<String> getNoticesIdsForUser(String id){
        Set<String> noticeIds = userRepository.findNoticeIdsByUserId(id);
        logger.debug(noticeIds);
        return noticeIds;
    }
    public Set<String> getReservationIdsForUser(String id){
        Set<String> reservationIds = userRepository.findReservationIdsByUserId(id);
        logger.debug(reservationIds);
        return reservationIds;
    }
    public Set<String> getItemIdsForUser(String id){
        Set<String> itemIds = userRepository.findItemIdsByUserId(id);
        logger.debug(itemIds);
        return itemIds;
    }
    
    public Page<UserDtoPublicPartial> getUsersByPropertyLikePublic(int pageNum, String value, UserFields property){
        switch (property) {
            case USERNAME:
                return userRepository.findPubUserDtoByUserNameLike(value, Pageable.ofSize(10).withPage(pageNum));
            case FIRSTNAME:
                return userRepository.findPubUserDtoByFirstNameLike(value, Pageable.ofSize(10).withPage(pageNum));
            case LASTNAME:
                return userRepository.findPubUserDtoByLastNameLike(value, Pageable.ofSize(10).withPage(pageNum));
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                
        }
    }
    public Page<UserDtoPrivatePartial> getUsersByPropertyLikePrivate(int pageNum, String value, UserFields property){
        switch (property) {
            case ID:
                return userRepository.findPrivateUserDtoByIdLike(value, Pageable.ofSize(10).withPage(pageNum));
            case USERNAME:
                return userRepository.findPrivateUserDtoByUserNameLike(value, Pageable.ofSize(10).withPage(pageNum));
            case FIRSTNAME:
                return userRepository.findPrivateUserDtoByFirstNameLike(value, Pageable.ofSize(10).withPage(pageNum));
            case LASTNAME:
                return userRepository.findPrivateUserDtoByLastNameLike(value, Pageable.ofSize(10).withPage(pageNum));
            case EMAIL:
                return userRepository.findPrivateUserDtoByEmailLike(value, Pageable.ofSize(10).withPage(pageNum));
            case PHONE:
                return userRepository.findPrivateUserDtoByPhoneLike(value, Pageable.ofSize(10).withPage(pageNum));
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                
        }
    }

    private UserDtoPublic mapMyUserToUserDtoPublic(MyUser user){
        return new UserDtoPublic(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(),user.getPath());
    }
    private UserDtoPrivatePartial mapMyUserToUserDtoPrivate(MyUser user){
        return new UserDtoPrivatePartial(user.getId(), user.getEmail(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getPhone());
    }

}
