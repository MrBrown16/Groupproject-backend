package hu.project.groupproject.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyVoteOption;



// @Repository
public interface VoteOptionRepository extends JpaRepository<MyVoteOption, Long>{
    
    // List<MyVoteOption> findByLastName(String lastName);

    // List<MyVoteOption> findByFirstNameLike(String firstName);
}
