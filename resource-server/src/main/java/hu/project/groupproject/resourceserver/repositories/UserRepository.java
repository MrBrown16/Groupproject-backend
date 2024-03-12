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
    Set<String> findOrgIdsByUserId(@Param("userId") String userId);

    @Query("SELECT post.id FROM MyUser u JOIN u.posts post WHERE u.id = :userId")
    Set<String> findPostIdsByUserId(@Param("userId") String userId);

    @Query("SELECT notice.id FROM MyUser u JOIN u.notices notice WHERE u.id = :userId")
    Set<String> findNoticeIdsByUserId(@Param("userId") String userId);

    @Query("SELECT reservation.id FROM MyUser u JOIN u.reservations reservation WHERE u.id = :userId")
    Set<String> findReservationIdsByUserId(@Param("userId") String userId);

    @Query("SELECT item.id FROM MyUser u JOIN u.items item WHERE u.id = :userId")
    Set<String> findItemIdsByUserId(@Param("userId") String userId);


    //TODO: create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"
}
