package hu.project.groupproject.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyUser;



public interface UserRepository extends JpaRepository<MyUser, Long>{
    
    <T> Optional<T> findById(Long id, Class<T> type);
    // <T> Optional<T> save(T user, Class<T> type);

}
