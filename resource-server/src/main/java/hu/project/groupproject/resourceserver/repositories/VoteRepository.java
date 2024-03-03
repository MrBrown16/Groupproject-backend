package hu.project.groupproject.resourceserver.repositories;


import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.votes.VoteDtoPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyVote;



public interface VoteRepository extends JpaRepository<MyVote, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    @Query("SELECT DISTINCT new hu.project.groupproject.resourceserver.dtos.En.votes.VoteDtoPartial(v.id, v.description, v.post.id) FROM MyVote v LEFT JOIN v.post WHERE v.id=:id")
    Optional<VoteDtoPartial> findVoteInfoById(@Param(value = "id") String id);

}
