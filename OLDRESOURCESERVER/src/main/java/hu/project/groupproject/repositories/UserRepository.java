package hu.project.groupproject.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.entities.MyUser;



public interface UserRepository extends JpaRepository<MyUser, Long>{
    
    <T> Optional<T> findById(Long id, Class<T> type);
    // <T> Optional<T> save(T user, Class<T> type);

    // @Query("SELECT oa.user_id FROM orgs_admins oa where oa.user_id=:adminId AND oa.org_id=:orgId")
    // Optional<Long> isAdmin(@Param("adminId") Long adminId, @Param("orgId") Long orgId);
    Optional<MyUser> findByOrgsIdAndId(Long orgId, Long adminId);

}
