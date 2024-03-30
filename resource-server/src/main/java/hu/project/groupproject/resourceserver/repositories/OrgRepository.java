package hu.project.groupproject.resourceserver.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.enums.OrgCategory;



public interface OrgRepository extends JpaRepository<MyOrg, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    @Query("SELECT event.id FROM MyOrg o JOIN o.events event WHERE o.id = :orgId")
    Set<String> findEventIdsByOrgId(@Param("orgId") String orgId);

    @Query("SELECT reservation.id FROM MyOrg o JOIN o.reservations reservation WHERE o.id = :orgId")
    Set<String> findReservationIdsByOrgId(@Param("orgId") String orgId);
    
    //TODO: create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"
    
    //name
    @Query("SELECT o "+
    "FROM MyOrg o "+
    "WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<MyOrg> findOrgByNameLike(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT o FROM MyOrg o WHERE :category MEMBER OF o.categories")
    Page<MyOrg> findOrgsByCategory(@Param("category") OrgCategory category, Pageable pageable);


    @Query("SELECT o "+
    "FROM MyOrg o LEFT JOIN o.users users "+
    "WHERE users.id = :userId")
    Page<MyOrg> findOrgByUserId(@Param("userId") String userId, Pageable pageable);
}
