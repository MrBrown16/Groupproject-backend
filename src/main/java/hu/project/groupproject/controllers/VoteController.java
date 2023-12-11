package hu.project.groupproject.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.entities.MyVote;
import hu.project.groupproject.services.VoteService;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



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

    @GetMapping
    public Optional<MyVote> getVote(@RequestParam(name = "id") Long id) {
        return voteService.getVote(id);
    }
    
    @PostMapping("/del")
    public void deleteVote(@RequestBody MyVote vote) {
        voteService.deleteVote(vote);
    }
    
}
