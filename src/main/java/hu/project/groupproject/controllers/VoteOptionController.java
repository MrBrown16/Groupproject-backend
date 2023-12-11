package hu.project.groupproject.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.entities.MyVoteOption;
import hu.project.groupproject.services.VoteOptionService;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/voteOption")
public class VoteOptionController {

    VoteOptionService voteOptionService;

    public VoteOptionController(VoteOptionService voteOptionService){
        this.voteOptionService=voteOptionService;
    }

    @PostMapping
    public MyVoteOption saveVoteOption(@RequestBody MyVoteOption voteOption){
        return voteOptionService.saveVoteOption(voteOption);
    }

    @GetMapping
    public Optional<MyVoteOption> getVoteOption(@RequestParam(name = "id") Long id) {
        return voteOptionService.getVoteOption(id);
    }
    
    @PostMapping("/del")
    public void deleteVoteOption(@RequestBody MyVoteOption voteOption) {
        voteOptionService.deleteVoteOption(voteOption);
    }
    
}
