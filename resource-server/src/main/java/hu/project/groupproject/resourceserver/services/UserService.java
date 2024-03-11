package hu.project.groupproject.resourceserver.services;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNew;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNewWithPW;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Service
public class UserService {
    
    protected final Log logger = LogFactory.getLog(getClass());

    UserRepository userRepository;

    @PersistenceContext
    EntityManager manager;
    
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
   
    public MyUser saveUser(MyUser myUser){
        return userRepository.save(myUser);
    }
    public Optional<UserDtoPublic> getUser(String id){
        return userRepository.findById(id, UserDtoPublic.class);
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
    public ImageUploadDetailsDto newUser(UserDtoNewWithPW newUser){
        if (newUser.PW() != null && newUser.phone() != null && newUser.email() != null && newUser.userName() != null && newUser.userName().length()>5) {
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
            UserDetails userDetails = User.builder().username(user.getUserName()).roles("USER").password(newUser.PW()).build();
            
            //TODO:save user in auth server (webClient http request to createNewUser) 
            String url= user.getPath();
            return new ImageUploadDetailsDto(url, false);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    @Transactional
    public ImageUploadDetailsDto updateUser(String id, UserDtoNew newUser){
        if (newUser.phone() != null && newUser.email() != null && newUser.userName() != null && newUser.userName().length()>5) {
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
    //OrgService has this 
    // @Transactional
    // public Boolean addUserToOrg(String adminId, String userId, String orgId){
    //     if (adminId != null && userId != null && orgId != null) {
    //         MyUser admin = manager.find(MyUser.class, adminId);
    //         MyUser user = manager.find(MyUser.class, userId);
    //         MyOrg org = manager.find(MyOrg.class, orgId);
    //         if (admin != null && user != null && org != null) {
    //             Optional<MyUser> foundUser = userRepository.findByOrgsIdAndId(orgId, adminId);
    //             if (foundUser.isPresent() && foundUser.get().getId() == adminId) {
    //                 org.addUser(user);
    //                 user.addOrg(org);
    //                 manager.flush();
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }

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
    
}
