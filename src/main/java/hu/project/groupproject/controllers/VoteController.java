package hu.project.groupproject.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.dtos.voteDTOs.VoteDTOPublic;
import hu.project.groupproject.entities.MyVote;
import hu.project.groupproject.services.VoteService;



@RestController
@RequestMapping("/vote")
public class VoteController {

    VoteService voteService;

    public VoteController(VoteService voteService){
        this.voteService=voteService;
    }

    @PostMapping
    public MyVote saveVote(@RequestBody MyVote vote){
        return voteService.saveVote(vote);
    }

    @GetMapping("/{id}")
    public Optional<VoteDTOPublic> getVote(@PathVariable Long id) {
        return voteService.getVote(id);
    }
    
    @PostMapping("/del")
    public void deleteVote(@RequestBody MyVote vote) {
        voteService.deleteVote(vote);
    }
    
}
