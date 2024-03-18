package hu.project.groupproject.resourceserver.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.enums.Category;



public interface OrgRepository extends JpaRepository<MyOrg, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    @Query("SELECT event.id FROM MyOrg o JOIN o.events event WHERE o.id = :orgId")
    Set<String> findEventIdsByOrgId(@Param("orgId") String orgId);

    @Query("SELECT reservation.id FROM MyOrg o JOIN o.reservations reservation WHERE o.id = :orgId")
    Set<String> findReservationIdsByOrgId(@Param("orgId") String orgId);
    
    //TODO: create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"
    
    //name
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublicPartial(o.id, o.name) "+
    "FROM MyOrg o JOIN o.reservations reservation "+
    "WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Set<OrgDtoPublicPartial> findOrgByNameLike(@Param("name") String name);

    @Query("SELECT o FROM MyOrg o WHERE :category MEMBER OF o.categories")
    Page<MyOrg> findByCategory(@Param("category") Category category, Pageable pageable);
}
