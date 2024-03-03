package hu.project.groupproject.resourceserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.posts.in.PostDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.votes.VoteDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.voteoptions.VoteOptionDtoPartial;

@RestController
@RequestMapping("/example")
public class ExampleController {
    


    //Post
    @GetMapping("/create-no-vote")//POST: /post/new
    public Optional<PostDtoCreate> getCreateNoVote() {
        return Optional.of(new PostDtoCreate(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Gyönyörű napunk van!", null, null));
    }
    @GetMapping("/create-with-vote")//POST: /post/new
    public Optional<PostDtoCreate> getCreateVote() {
        String[] optionTexts ={"Sok fa legyen","maximum kettő fa legyen","mi az a fa?"};
        return Optional.of(new PostDtoCreate(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "szöveg a város egyik parkjában való fejlesztésről, és közvéleménykutatés hogy hány fa legyen", "válassz az opciók közül/ melyik opció tetszik jobban?", optionTexts));
    }


    //Vote
    @GetMapping("/vote")//PUT: /vote/{id}
    public Optional<VoteDtoPublic> updateVote() {
        VoteOptionDtoPartial vo1 = new VoteOptionDtoPartial(UUID.randomUUID().toString(), "new text for existing option (votesNum does not matter will be set to 0)", 111L);
        List<VoteOptionDtoPartial> options = new ArrayList<VoteOptionDtoPartial>();
        options.add(vo1);
        vo1 = new VoteOptionDtoPartial(null, "text For new voteOption (votesNum does not matter, will be set to 0)",111L);
        options.add(vo1);
        return Optional.of(new VoteDtoPublic(UUID.randomUUID().toString(), "Example vote text you decide what should be here!", options, UUID.randomUUID().toString()));
    }

}
