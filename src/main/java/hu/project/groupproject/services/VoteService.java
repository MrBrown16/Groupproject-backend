package hu.project.groupproject.services;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.voteDTOs.VoteDTOPartial;
import hu.project.groupproject.dtos.voteDTOs.VoteDTOPublic;
import hu.project.groupproject.dtos.voteOptionsDTOs.VoteOptionDTOInternal;
import hu.project.groupproject.entities.MyVote;
import hu.project.groupproject.repositories.VoteOptionRepository;
import hu.project.groupproject.repositories.VoteRepository;


@Service
public class VoteService {
    
    VoteRepository voteRepository;
    VoteOptionRepository voteOptionRepository;

    
    public VoteService(VoteRepository voteRepository, VoteOptionRepository voteOptionRepository){
        this.voteRepository=voteRepository;
        this.voteOptionRepository=voteOptionRepository;
    }
   
    public MyVote saveVote(MyVote vote){
        return voteRepository.save(vote);
    }
    // public Optional<MyVote> getVote(Long id){
    //     return voteRepository.findById(id, MyVote.class);
    // }

    public Optional<VoteDTOPublic> getVote(Long id){
        Optional<VoteDTOPartial> info = voteRepository.findVoteInfoById(id);
        if (info.isPresent()) {
            System.out.println("-------------------------------info[0]: "+info+" : "+info.get()+"---------------------");
            List<VoteOptionDTOInternal> opt = voteOptionRepository.findVoteOptionDtosByvoteId(id);
            return Optional.of( new VoteDTOPublic(info.get().id(), (String) info.get().description(), (List<VoteOptionDTOInternal>) opt, (Long) info.get().postId()));    
        }else{
            return Optional.empty();
        }
    }

    public void deleteVote(MyVote vote){
        voteRepository.delete(vote);
    }




}
