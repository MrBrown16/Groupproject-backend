package hu.project.groupproject.resourceserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;



public interface OrgRepository extends JpaRepository<MyOrg, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

}
