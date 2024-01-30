package hu.project.groupproject.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.dtos.postDTOs.in.PostDTOCreate;
import hu.project.groupproject.dtos.voteDTOs.VoteDTOPublic;
import hu.project.groupproject.dtos.voteOptionsDTOs.VoteOptionDTOInternal;

@RestController
@RequestMapping("/example")
public class ExampleController {
    


    //Post
    @GetMapping("/create-no-vote")//POST: /post/new
    public Optional<PostDTOCreate> getCreateNoVote() {
        return Optional.of(new PostDTOCreate(2L, 2L, "Gyönyörű napunk van!", null, null));
    }
    @GetMapping("/create-with-vote")//POST: /post/new
    public Optional<PostDTOCreate> getCreateVote() {
        String[] optionTexts ={"Sok fa legyen","maximum kettő fa legyen","mi az a fa?"};
        return Optional.of(new PostDTOCreate(2L, 2L, "szöveg a város egyik parkjában való fejlesztésről, és közvéleménykutatés hogy hány fa legyen", "válassz az opciók közül/ melyik opció tetszik jobban?", optionTexts));
    }


    //Vote
    @GetMapping("/vote")//PUT: /vote/{id}
    public Optional<VoteDTOPublic> updateVote() {
        VoteOptionDTOInternal vo1 = new VoteOptionDTOInternal(1L, "new text for existing option (votesNum doesw not matter will be set to 0)", 111L);
        List<VoteOptionDTOInternal> options = new ArrayList<VoteOptionDTOInternal>();
        options.add(vo1);
        vo1 = new VoteOptionDTOInternal(null, "text For new voteOption (votesNum does not matter, will be set to 0)",111L);
        options.add(vo1);
        return Optional.of(new VoteDTOPublic(2L, "Example vote text you decide what should be here!", options, 2L));
    }

}
