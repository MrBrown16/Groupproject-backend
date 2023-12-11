package hu.project.groupproject.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyUser;



// @Repository
public interface UserRepository extends JpaRepository<MyUser, Long>{
    
    // List<MyUser> findByLastName(String lastName);

    // List<MyUser> findByFirstNameLike(String firstName);
}
