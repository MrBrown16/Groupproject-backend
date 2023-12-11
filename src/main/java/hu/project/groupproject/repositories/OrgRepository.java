package hu.project.groupproject.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyOrg;



// @Repository
public interface OrgRepository extends JpaRepository<MyOrg, Long>{
    
    // List<MyOrg> findByLastName(String lastName);

    // List<MyOrg> findByFirstNameLike(String firstName);
}
