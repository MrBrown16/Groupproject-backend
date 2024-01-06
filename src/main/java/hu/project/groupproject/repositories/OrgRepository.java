package hu.project.groupproject.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyOrg;



// @Repository
public interface OrgRepository extends JpaRepository<MyOrg, Long>{
    
    <T> Optional<T> findById(Long id, Class<T> type);

    // List<MyOrg> findByLastName(String lastName);

    // List<MyOrg> findByFirstNameLike(String firstName);
}
