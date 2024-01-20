package hu.project.groupproject.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.dtos.voteDTOs.VoteDTOPublic;
import hu.project.groupproject.services.VoteService;

//ALL done!!! :)

@RestController
@RequestMapping("/vote")
public class VoteController {

    VoteService voteService;

    public VoteController(VoteService voteService){
        this.voteService=voteService;
    }


    @GetMapping("/{id}")
    public Optional<VoteDTOPublic> getVote(@PathVariable Long id) {
        return voteService.getVote(id);
    }


    @PutMapping("/{id}")
    public Optional<VoteDTOPublic> updateVote(@PathVariable Long id, @RequestBody VoteDTOPublic vote){
        return voteService.updateVote(id, vote);
    }

    
    @DeleteMapping("/del/{id}")
    public void deleteVote(@PathVariable Long id) {
        voteService.deleteVote(id);
    }
    
}
