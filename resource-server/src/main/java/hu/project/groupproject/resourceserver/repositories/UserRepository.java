package hu.project.groupproject.resourceserver.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;



public interface UserRepository extends JpaRepository<MyUser, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);
    // <T> Optional<T> save(T user, Class<T> type);

    // @Query("SELECT oa.user_id FROM orgs_admins oa where oa.user_id=:adminId AND oa.org_id=:orgId")
    // Optional<Long> isAdmin(@Param("adminId") Long adminId, @Param("orgId") Long orgId);
    Optional<MyUser> findByOrgsIdAndId(String orgId, String adminId);
    
    Optional<MyUser> findByUserName(String username);

}
