package hu.project.groupproject.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.entities.MyVote;



// @Repository
public interface VoteRepository extends JpaRepository<MyVote, Long>{
    
    <T> Optional<T> findById(Long id, Class<T> type);

    // List<MyVote> findByLastName(String lastName);

    // List<MyVote> findByFirstNameLike(String firstName);
}
