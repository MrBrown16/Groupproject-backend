package hu.project.groupproject.resourceserver.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.En.voteoptions.VoteOptionDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyVoteOption;
import hu.project.groupproject.resourceserver.repositories.VoteOptionRepository;


@Service //possibly deletable voteOptionRepository can do everything
public class VoteOptionService {
    
    VoteOptionRepository voteOptionRepository;

    
    public VoteOptionService(VoteOptionRepository voteOptionRepository){
        this.voteOptionRepository=voteOptionRepository;
    }
   
    public MyVoteOption saveVoteOption(MyVoteOption voteOption){
        return voteOptionRepository.save(voteOption);
    }
    public Optional<VoteOptionDtoPublic> getVoteOption(String id){
        return voteOptionRepository.findById(id, VoteOptionDtoPublic.class);
    }

    public void deleteVoteOption(MyVoteOption voteOption){
        voteOptionRepository.delete(voteOption);
    }




}
