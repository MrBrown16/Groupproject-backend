package hu.project.groupproject.resourceserver.repositories;


import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;



public interface UserRepository extends JpaRepository<MyUser, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    Optional<MyUser> findByOrgsIdAndId(String orgId, String adminId);
    
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial(u.id, u.userName, u.firstName, u.lastName) from MyUser u where u.userName=:username")
    Optional<UserDtoPublicPartial> findByUserName(String username);

    @Query("SELECT org.id FROM MyUser u JOIN u.orgs org WHERE u.id = :userId")
    Set<String> findOrgIdByUserId(@Param("userId") String userId);


}
