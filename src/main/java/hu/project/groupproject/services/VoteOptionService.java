package hu.project.groupproject.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.voteOptionsDTOs.VoteOptionDTOPublic;
import hu.project.groupproject.entities.MyVoteOption;
import hu.project.groupproject.repositories.VoteOptionRepository;


@Service
public class VoteOptionService {
    
    VoteOptionRepository voteOptionRepository;

    
    public VoteOptionService(VoteOptionRepository voteOptionRepository){
        this.voteOptionRepository=voteOptionRepository;
    }
   
    public MyVoteOption saveVoteOption(MyVoteOption voteOption){
        return voteOptionRepository.save(voteOption);
    }
    public Optional<VoteOptionDTOPublic> getVoteOption(Long id){
        return voteOptionRepository.findById(id, VoteOptionDTOPublic.class);
    }

    public void deleteVoteOption(MyVoteOption voteOption){
        voteOptionRepository.delete(voteOption);
    }




}
