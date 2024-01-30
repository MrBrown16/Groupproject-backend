package hu.project.groupproject.services;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.voteDTOs.VoteDTOPartial;
import hu.project.groupproject.dtos.voteDTOs.VoteDTOPublic;
import hu.project.groupproject.dtos.voteOptionsDTOs.VoteOptionDTOInternal;
import hu.project.groupproject.entities.MyVote;
import hu.project.groupproject.entities.MyVoteOption;
import hu.project.groupproject.repositories.VoteOptionRepository;
import hu.project.groupproject.repositories.VoteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Service
public class VoteService {
    
    VoteRepository voteRepository;
    VoteOptionRepository voteOptionRepository;

    @PersistenceContext
    EntityManager entityManager;

    public VoteService(VoteRepository voteRepository, VoteOptionRepository voteOptionRepository){
        this.voteRepository=voteRepository;
        this.voteOptionRepository=voteOptionRepository;
    }
   
    @Transactional
    public Optional<VoteDTOPublic> updateVote(Long id, VoteDTOPublic voteDto){
        if (id != null && id == voteDto.id() && id > 0 ) {//&& voteRepository.existsById(id)
            MyVote vote = entityManager.find(MyVote.class, voteDto.id());
            if (vote != null) {
                
                if (voteDto.description()!=null) {
                    vote.setDescription(voteDto.description());
                }

                if (voteDto.options().size()>0) {
                    List<MyVoteOption> options = new ArrayList<>(voteDto.options().size());
                    for (VoteOptionDTOInternal voteOption : voteDto.options()) {
                        if (voteOption.id()!=null && voteOption.optionText() != null && voteOption.optionText().length()>=3) {// && voteOptionRepository.existsById(voteOption.id())
                            MyVoteOption in = entityManager.find(MyVoteOption.class, voteOption.id());
                            if (in!=null) {   
                                in.setOptionText(voteOption.optionText());
                                in.setVotesNum(0L);
                                options.add(in);
                            }
                        }else if (voteOption.optionText() != null && voteOption.optionText().length()>=3) {
                            MyVoteOption newOpt = new MyVoteOption();
                            newOpt.setVotesNum(0L); 
                            newOpt = voteOptionRepository.save(newOpt);
                            newOpt.setOptionText(voteOption.optionText());
                            options.add(newOpt);
                        }else if (voteOption.id() != null && voteOptionRepository.existsById(voteOption.id()) && voteOption.optionText() == null || voteOption.optionText().length()<3) {
                           voteOptionRepository.deleteById(voteOption.id());
                        }
                    }
                    vote.setOptions(options);
                }else{
                    //there should be no options 
                    List<Long> deletableIds = voteOptionRepository.findVoteOptionIdByVoteId(voteDto.id());
                    voteOptionRepository.deleteAllById(deletableIds);
                }
                //postId ignored, can't be changed 
                entityManager.flush();
                return getVote(id);
            }

        }
        return Optional.empty();
    }

    public void saveVote(MyVote vote){
        voteRepository.save(vote);
    }


    public Optional<VoteDTOPublic> getVote(Long id){
        Optional<VoteDTOPartial> info = voteRepository.findVoteInfoById(id);
        if (info.isPresent()) {
            // System.out.println("-------------------------------info[0]: "+info+" : "+info.get()+"---------------------");
            List<VoteOptionDTOInternal> opt = voteOptionRepository.findVoteOptionDtosByvoteId(id);
            return Optional.of( new VoteDTOPublic(info.get().id(), (String) info.get().description(), (List<VoteOptionDTOInternal>) opt, (Long) info.get().postId()));    
        }else{
            return Optional.empty();
        }
    }

    public void deleteVote(Long voteId){
        voteRepository.deleteById(voteId);
    }




}
