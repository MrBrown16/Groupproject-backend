package hu.project.groupproject.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.dtos.postDTOs.in.PostDTOCreate;
import hu.project.groupproject.dtos.postDTOs.in.PostDTOUpdate;
import hu.project.groupproject.dtos.postDTOs.out.PostDTOPublic;
import hu.project.groupproject.dtos.postDTOs.out.PostDTOPublicExtended;
import hu.project.groupproject.entities.MyPost;
import hu.project.groupproject.services.PostService;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/post")
public class PostController {

    PostService postService;

    public PostController(PostService postService){
        this.postService=postService;
    }

    
    @PostMapping  
    public boolean savePost(@RequestBody PostDTOCreate post){
        return postService.savePost(post);
    }

    @PostMapping("/{id}") //TODO:new dto to get user id, org id, post contents, image...  
    public boolean updatePost(@PathVariable Long id,@RequestBody PostDTOUpdate post){
        return postService.updatePost(id,post);
        // return false;
    }

    @GetMapping("/create-no-vote")
    public Optional<PostDTOCreate> getCreateNoVote() {
        return Optional.of(new PostDTOCreate(2L, 2L, "Gyönyörű napunk van!", null, null));
    }
    @GetMapping("/create-with-vote")
    public Optional<PostDTOCreate> getCreateVote() {
        String[] optionTexts ={"Sok fa legyen","maximum kettő fa legyen","mi az a fa?"};
        return Optional.of(new PostDTOCreate(2L, 2L, "szöveg a város egyik parkjában való fejlesztésről, és közvéleménykutatés hogy hány fa legyen", "válassz az opciók közül/ melyik opció tetszik jobban?", optionTexts));
    }
    @GetMapping("/{id}/short")
    public Optional<PostDTOPublic> getPostShort(@PathVariable Long id) {
        return postService.getPostShort(id);
    }
    @GetMapping("/{id}/ex")
    public Optional<PostDTOPublicExtended> getPostEx(@PathVariable Long id) {
        return postService.getPostExtended(id);
    }
    
    @PostMapping("/del")
    public void deletePost(@RequestBody MyPost post) {
        postService.deletePost(post);
    }
    
}
