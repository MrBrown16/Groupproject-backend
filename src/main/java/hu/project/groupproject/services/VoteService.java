package hu.project.groupproject.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import hu.project.groupproject.entities.MyVote;
import hu.project.groupproject.repositories.VoteRepository;


@Service
public class VoteService {
    
    VoteRepository voteRepository;

    // @PersistenceContext
    // EntityManager entityManager;
    
    public VoteService(VoteRepository voteRepository){
        this.voteRepository=voteRepository;
    }
   
    public MyVote saveVote(MyVote vote){
        return voteRepository.save(vote);
    }
    public Optional<MyVote> getVote(Long id){
        return voteRepository.findById(id);
    }

    public void deleteVote(MyVote vote){
        voteRepository.delete(vote);
    }




}
