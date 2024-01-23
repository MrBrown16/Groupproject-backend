package hu.project.groupproject.services;

import java.util.Optional;

import org.hibernate.annotations.NaturalId;
import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.userDTOs.UserDTONEW;
import hu.project.groupproject.dtos.userDTOs.UserDTOPublic;
import hu.project.groupproject.entities.MyOrg;
import hu.project.groupproject.entities.MyUser;
import hu.project.groupproject.repositories.UserRepository;
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
    public Optional<UserDTOPublic> getUser(Long id){
        return userRepository.findById(id, UserDTOPublic.class);
    }

    public void deleteUser(MyUser user){
        userRepository.delete(user);
    }

    @Transactional
    public Boolean newUser(UserDTONEW newUser){
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
    public Boolean addUserToOrg(Long adminId, Long userId, Long orgId){
        if (adminId != null && userId != null && orgId != null && adminId > 0 && userId > 0 && orgId > 0) {
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
    // public MyUser doWork(){

    //     MyUser user = new MyUser();
    //     // user.setId(null);
    //     user.setUserName("ogie");
    //     user.setFirstName("Oliver");
    //     user.setLastName("Gierke");
    //     user.setEmail("oliver.gierke@gmail.com");
    //     user.setPhone(1122334455L);
    //     user.setProfileImagePath("/user/profile/img");

    //     System.out.println("-----------------"+user.toString()+"-----------------");
    //     return userRepository.save(user);
    //     // entityManager.persist(user);

    //     // List<MyUser> lastNameResults = userRepository.findByLastName("Gierke");
    //     // List<MyUser> firstNameResults = userRepository.findByFirstNameLike("Oli*");
    //     // System.out.println(lastNameResults.get(0)+" "+firstNameResults.get(0));
    // }
    
}
