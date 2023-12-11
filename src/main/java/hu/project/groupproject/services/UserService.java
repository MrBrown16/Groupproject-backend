package hu.project.groupproject.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import hu.project.groupproject.entities.MyUser;
import hu.project.groupproject.repositories.UserRepository;


@Service
public class UserService {
    
    UserRepository userRepository;

    // @PersistenceContext
    // EntityManager entityManager;
    
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
   
    public MyUser saveUser(MyUser myUser){
        return userRepository.save(myUser);
    }
    public Optional<MyUser> getUser(Long id){
        return userRepository.findById(id);
    }

    public void deleteUser(MyUser user){
        userRepository.delete(user);
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
