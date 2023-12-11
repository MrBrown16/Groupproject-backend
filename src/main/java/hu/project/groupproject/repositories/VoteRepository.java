package hu.project.groupproject.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyVote;



// @Repository
public interface VoteRepository extends JpaRepository<MyVote, Long>{
    
    // List<MyVote> findByLastName(String lastName);

    // List<MyVote> findByFirstNameLike(String firstName);
}
