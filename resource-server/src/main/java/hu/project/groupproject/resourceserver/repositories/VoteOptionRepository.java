package hu.project.groupproject.resourceserver.repositories;


import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.voteoptions.VoteOptionDtoPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyVoteOption;



public interface VoteOptionRepository extends JpaRepository<MyVoteOption, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.voteoptions.VoteOptionDtoPartial(o.id, o.optionText, o.votesNum) FROM MyVoteOption o LEFT JOIN o.vote WHERE o.vote.id=:id")
    List<VoteOptionDtoPartial> findVoteOptionDtosByvoteId(@Param(value = "id") String id);


    @Query("SELECT o.id FROM MyVoteOption o WHERE o.vote.id=:id")
    List<String> findVoteOptionIdByVoteId(@Param(value = "id") String id);
}
