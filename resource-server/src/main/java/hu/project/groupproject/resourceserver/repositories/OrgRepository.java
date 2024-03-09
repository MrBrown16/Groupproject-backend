package hu.project.groupproject.resourceserver.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;



public interface OrgRepository extends JpaRepository<MyOrg, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    @Query("SELECT event.id FROM MyOrg o JOIN o.events event WHERE o.id = :orgId")
    Set<String> findEventIdByOrgId(@Param("orgId") String orgId);
}
