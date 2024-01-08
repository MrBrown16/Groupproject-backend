package hu.project.groupproject.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.dtos.postDTOs.PostDTOPublic;
import hu.project.groupproject.dtos.postDTOs.PostDTOPublicExtended;
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
    public MyPost savePost(@RequestBody MyPost post){
        return postService.savePost(post);
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
