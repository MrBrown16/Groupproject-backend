// package hu.project.groupproject.resourceserver.services;

// import java.util.Optional;
// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.stereotype.Service;

// import hu.project.groupproject.resourceserver.dtos.En.votes.VoteDtoPartial;
// import hu.project.groupproject.resourceserver.dtos.En.votes.VoteDtoPublic;
// import hu.project.groupproject.resourceserver.dtos.En.voteoptions.VoteOptionDtoPartial;
// import hu.project.groupproject.resourceserver.entities.softdeletable.MyVote;
// import hu.project.groupproject.resourceserver.entities.softdeletable.MyVoteOption;
// import hu.project.groupproject.resourceserver.repositories.VoteOptionRepository;
// import hu.project.groupproject.resourceserver.repositories.VoteRepository;
// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;
// import jakarta.transaction.Transactional;


// @Service
// public class VoteService {
    
//     VoteRepository voteRepository;
//     VoteOptionRepository voteOptionRepository;

//     @PersistenceContext
//     EntityManager entityManager;

//     public VoteService(VoteRepository voteRepository, VoteOptionRepository voteOptionRepository){
//         this.voteRepository=voteRepository;
//         this.voteOptionRepository=voteOptionRepository;
//     }
   
//     @SuppressWarnings("null")
//     @Transactional
//     public Optional<VoteDtoPublic> updateVote(String id, VoteDtoPublic voteDto){
//         if (id != null && id == voteDto.id()) {//&& voteRepository.existsById(id)
//             MyVote vote = entityManager.find(MyVote.class, voteDto.id());
//             if (vote != null) {
                
//                 if (voteDto.description()!=null) {
//                     vote.setDescription(voteDto.description());
//                 }

//                 if (voteDto.options().size()>0) {
//                     List<MyVoteOption> options = new ArrayList<>(voteDto.options().size());
//                     for (VoteOptionDtoPartial voteOption : voteDto.options()) {
//                         if (voteOption.id()!=null && voteOption.optionText() != null && voteOption.optionText().length()>=3) {// && voteOptionRepository.existsById(voteOption.id())
//                             MyVoteOption in = entityManager.find(MyVoteOption.class, voteOption.id());
//                             if (in!=null) {   
//                                 in.setOptionText(voteOption.optionText());
//                                 in.setVotesNum(0L);
//                                 options.add(in);
//                             }
//                         }else if (voteOption.optionText() != null && voteOption.optionText().length()>=3) {
//                             MyVoteOption newOpt = new MyVoteOption();
//                             newOpt.setVotesNum(0L); 
//                             newOpt = voteOptionRepository.save(newOpt);
//                             newOpt.setOptionText(voteOption.optionText());
//                             options.add(newOpt);
//                         }else if (voteOption.id() != null && voteOptionRepository.existsById(voteOption.id()) && voteOption.optionText() == null || voteOption.optionText().length()<3) {
//                            voteOptionRepository.deleteById(voteOption.id());
//                         }
//                     }
//                     vote.setOptions(options);
//                 }else{
//                     //there should be no options 
//                     List<String> deletableIds = voteOptionRepository.findVoteOptionIdByVoteId(voteDto.id());
//                     voteOptionRepository.deleteAllById(deletableIds);
//                 }
//                 //postId ignored, can't be changed 
//                 entityManager.flush();
//                 return getVote(id);
//             }

//         }
//         return Optional.empty();
//     }

//     public void saveVote(MyVote vote){
//         voteRepository.save(vote);
//     }


//     public Optional<VoteDtoPublic> getVote(String id){
//         Optional<VoteDtoPartial> info = voteRepository.findVoteInfoById(id);
//         if (info.isPresent()) {
//             // System.out.println("-------------------------------info[0]: "+info+" : "+info.get()+"---------------------");
//             List<VoteOptionDtoPartial> opt = voteOptionRepository.findVoteOptionDtosByvoteId(id);
//             return Optional.of( new VoteDtoPublic(info.get().id(), (String) info.get().description(), (List<VoteOptionDtoPartial>) opt,  info.get().postId()));    
//         }else{
//             return Optional.empty();
//         }
//     }

//     public void deleteVote(String voteId){
//         voteRepository.deleteById(voteId);
//     }




// }
