package hu.project.groupproject.resourceserver.services;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNew;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
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
                String profile = user.getPath();
                UserDtoPublic pub = new UserDtoPublic(part.id(), part.userName(), part.firstName(), part.lastName(), profile);
                return Optional.of(pub);
            }
        }
        return Optional.empty();
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    @Transactional
    public Optional<MyUser> newUser(UserDtoNew newUser){
        if (newUser.phone() != null && newUser.email() != null && newUser.userName() != null && newUser.userName().length()>5) {
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
            return Optional.of(user);
        }else {
            return Optional.empty();
        }
    }
    @Transactional
    public Optional<MyUser> updateUser(String id, UserDtoNew newUser){
        if (newUser.phone() != null && newUser.email() != null && newUser.userName() != null && newUser.userName().length()>5) {
            MyUser user = manager.find(MyUser.class, id);
            if (user == null) {
                return Optional.empty();
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
            return Optional.of(user);
        }else {
            return Optional.empty();
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
        Set<String> orgIds = userRepository.findOrgIdByUserId(id);
        logger.debug(orgIds);
        return orgIds;
    }
    public Set<String> getPostsIdsForUser(String id){
        Set<String> postIds = userRepository.findPostIdByUserId(id);
        logger.debug(postIds);
        return postIds;
    }
    
}
