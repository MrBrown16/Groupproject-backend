package hu.project.groupproject.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyVoteOption;



// @Repository
public interface VoteOptionRepository extends JpaRepository<MyVoteOption, Long>{
    
    <T> Optional<T> findById(Long id, Class<T> type);

    // List<MyVoteOption> findByLastName(String lastName);

    // List<MyVoteOption> findByFirstNameLike(String firstName);
}
