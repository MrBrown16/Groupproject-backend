package hu.project.groupproject.resourceserver.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.votes.VoteDtoPublic;
import hu.project.groupproject.resourceserver.services.VoteService;

//ALL done!!! :)

@RestController
@RequestMapping("/vote")
public class VoteController {

    VoteService voteService;

    public VoteController(VoteService voteService){
        this.voteService=voteService;
    }


    @GetMapping("/{id}")
    public Optional<VoteDtoPublic> getVote(@PathVariable String id) {
        return voteService.getVote(id);
    }


    @PutMapping("/{id}")
    public Optional<VoteDtoPublic> updateVote(@PathVariable String id, @RequestBody VoteDtoPublic vote){
        return voteService.updateVote(id, vote);
    }

    
    @DeleteMapping("/del/{id}")
    public void deleteVote(@PathVariable String id) {
        voteService.deleteVote(id);
    }
    
}
