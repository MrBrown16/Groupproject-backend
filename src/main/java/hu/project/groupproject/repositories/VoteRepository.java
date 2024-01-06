package hu.project.groupproject.repositories;


import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.dtos.voteDTOs.VoteDTOPartial;
import hu.project.groupproject.entities.MyVote;



public interface VoteRepository extends JpaRepository<MyVote, Long>{
    
    <T> Optional<T> findById(Long id, Class<T> type);

    @Query("SELECT DISTINCT new hu.project.groupproject.dtos.voteDTOs.VoteDTOPartial(v.id, v.description, v.post.id) FROM MyVote v LEFT JOIN v.post WHERE v.id=:id")
    Optional<VoteDTOPartial> findVoteInfoById(@Param(value = "id") Long id);

}
