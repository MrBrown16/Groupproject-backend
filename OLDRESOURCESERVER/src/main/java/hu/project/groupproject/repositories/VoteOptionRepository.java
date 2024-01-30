package hu.project.groupproject.repositories;


import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.dtos.voteOptionsDTOs.VoteOptionDTOInternal;
import hu.project.groupproject.entities.MyVoteOption;



public interface VoteOptionRepository extends JpaRepository<MyVoteOption, Long>{
    
    <T> Optional<T> findById(Long id, Class<T> type);

    @Query("SELECT new hu.project.groupproject.dtos.voteOptionsDTOs.VoteOptionDTOInternal(o.id, o.optionText, o.votesNum) FROM MyVoteOption o LEFT JOIN o.vote WHERE o.vote.id=:id")
    List<VoteOptionDTOInternal> findVoteOptionDtosByvoteId(@Param(value = "id") Long id);


    @Query("SELECT o.id FROM MyVoteOption o WHERE o.vote.id=:id")
    List<Long> findVoteOptionIdByVoteId(@Param(value = "id") Long id);
}
