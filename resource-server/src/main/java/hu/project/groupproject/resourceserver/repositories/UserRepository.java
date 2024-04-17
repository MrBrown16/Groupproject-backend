package hu.project.groupproject.resourceserver.repositories;


import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;



public interface UserRepository extends JpaRepository<MyUser, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    Optional<MyUser> findByOrgsIdAndId(String orgId, String adminId);

    
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial(u.id, u.userName, u.firstName, u.lastName) "+
    "from MyUser u "+
    "where u.userName=:username")
    Optional<UserDtoPublicPartial> findByUserName(@Param("username") String username);

    @Query("SELECT org.id FROM MyUser u JOIN u.orgs org WHERE u.id = :userId")
    Set<String> findOrgIdsByUserId(@Param("userId") String userId);

    @Query("SELECT post.id FROM MyUser u JOIN u.posts post WHERE u.id = :userId")
    Set<String> findPostIdsByUserId(@Param("userId") String userId);

    @Query("SELECT news.id FROM MyUser u JOIN u.news news WHERE u.id = :userId")
    Set<String> findNewsIdsByUserId(@Param("userId") String userId);

    @Query("SELECT notice.id FROM MyUser u JOIN u.notices notice WHERE u.id = :userId")
    Set<String> findNoticeIdsByUserId(@Param("userId") String userId);

    @Query("SELECT reservation.id FROM MyUser u JOIN u.reservations reservation WHERE u.id = :userId")
    Set<String> findReservationIdsByUserId(@Param("userId") String userId);

    @Query("SELECT item.id FROM MyUser u JOIN u.items item WHERE u.id = :userId")
    Set<String> findItemIdsByUserId(@Param("userId") String userId);


    //Done create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"

    //userName
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial(u.id, u.userName, u.firstName, u.lastName) "+
    "from MyUser u "+
    "WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<UserDtoPublicPartial> findPubUserDtoByUserNameLike(@Param(value = "name") String name, Pageable pageable);
    
    //firstName
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial(u.id, u.userName, u.firstName, u.lastName) "+
    "from MyUser u "+
    "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<UserDtoPublicPartial> findPubUserDtoByFirstNameLike(@Param(value = "name") String name, Pageable pageable);
    
    //lastName
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublicPartial(u.id, u.userName, u.firstName, u.lastName) "+
    "from MyUser u "+
    "WHERE LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<UserDtoPublicPartial> findPubUserDtoByLastNameLike(@Param(value = "name") String name, Pageable pageable);
    
    //userName private
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial(u.id, u.email, u.userName, u.firstName, u.lastName, u.phone ) "+
    "from MyUser u "+
    "WHERE LOWER(u.id) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<UserDtoPrivatePartial> findPrivateUserDtoByIdLike(@Param(value = "name") String name, Pageable pageable);
    //userName private
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial(u.id, u.email, u.userName, u.firstName, u.lastName, u.phone ) "+
    "from MyUser u "+
    "WHERE LOWER(u.userName) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<UserDtoPrivatePartial> findPrivateUserDtoByUserNameLike(@Param(value = "name") String name, Pageable pageable);
    
    //firstName private
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial(u.id, u.email, u.userName, u.firstName, u.lastName, u.phone ) "+
    "from MyUser u "+
    "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<UserDtoPrivatePartial> findPrivateUserDtoByFirstNameLike(@Param(value = "name") String name, Pageable pageable);
    
    //lastName private
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial(u.id, u.email, u.userName, u.firstName, u.lastName, u.phone ) "+
    "from MyUser u "+
    "WHERE LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<UserDtoPrivatePartial> findPrivateUserDtoByLastNameLike(@Param(value = "name") String name, Pageable pageable);
    
    //phone private
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial(u.id, u.email, u.userName, u.firstName, u.lastName, u.phone ) "+
    "from MyUser u "+
    "WHERE CAST(u.phone AS string) LIKE LOWER(CONCAT('%', :phone, '%')) ")
    Page<UserDtoPrivatePartial> findPrivateUserDtoByPhoneLike(@Param(value = "phone") String phone, Pageable pageable);
    
    //email private
    @Query("Select new hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPrivatePartial(u.id, u.email, u.userName, u.firstName, u.lastName, u.phone ) "+
    "from MyUser u "+
    "WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) ")
    Page<UserDtoPrivatePartial> findPrivateUserDtoByEmailLike(@Param(value = "email") String email, Pageable pageable);
}
