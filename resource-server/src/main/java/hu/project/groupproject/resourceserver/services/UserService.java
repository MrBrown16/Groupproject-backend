package hu.project.groupproject.resourceserver.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoNew;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Service
public class UserService {
    
    UserRepository userRepository;

    @PersistenceContext
    EntityManager entityManager;
    
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
   
    public MyUser saveUser(MyUser myUser){
        return userRepository.save(myUser);
    }
    public Optional<UserDtoPublic> getUser(String id){
        return userRepository.findById(id, UserDtoPublic.class);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    @Transactional
    public Boolean newUser(UserDtoNew newUser){
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
            userRepository.save(user);
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    public Boolean addUserToOrg(String adminId, String userId, String orgId){
        if (adminId != null && userId != null && orgId != null) {
            MyUser admin = entityManager.find(MyUser.class, adminId);
            MyUser user = entityManager.find(MyUser.class, userId);
            MyOrg org = entityManager.find(MyOrg.class, orgId);
            if (admin != null && user != null && org != null) {
                Optional<MyUser> foundUser = userRepository.findByOrgsIdAndId(orgId, adminId);
                if (foundUser.isPresent() && foundUser.get().getId() == adminId) {
                    org.addUser(user);
                    user.addOrg(org);
                    entityManager.flush();
                    return true;
                }
            }
        }
        return false;
    }

    
}
