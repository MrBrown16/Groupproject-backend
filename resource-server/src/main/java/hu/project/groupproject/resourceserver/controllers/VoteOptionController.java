// package hu.project.groupproject.resourceserver.controllers;

// import org.springframework.web.bind.annotation.RestController;

// import hu.project.groupproject.resourceserver.dtos.En.voteoptions.VoteOptionDtoPublic;
// import hu.project.groupproject.resourceserver.entities.softdeletable.MyVoteOption;
// import hu.project.groupproject.resourceserver.services.VoteOptionService;

// import java.util.Optional;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;



// @RestController
// @RequestMapping("/voteOption")
// public class VoteOptionController {

// //ONLY purpose would be testing, no longer needed/an option


//     VoteOptionService voteOptionService;

//     public VoteOptionController(VoteOptionService voteOptionService){
//         this.voteOptionService=voteOptionService;
//     }

//     @PostMapping
//     public MyVoteOption saveVoteOption(@RequestBody MyVoteOption voteOption){
//         return voteOptionService.saveVoteOption(voteOption);
//     }

//     @GetMapping("/{id}")
//     public Optional<VoteOptionDTOPublic> getVoteOption(@PathVariable Long id) {
//         return voteOptionService.getVoteOption(id);
//     }
    
//     @DeleteMapping("/del")
//     public void deleteVoteOption(@RequestBody MyVoteOption voteOption) {
//         voteOptionService.deleteVoteOption(voteOption);
//     }
    
// }
