package hu.project.groupproject.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.dtos.postDTOs.in.PostDTOCreate;
import hu.project.groupproject.dtos.postDTOs.in.PostDTOUpdate;
import hu.project.groupproject.dtos.postDTOs.out.PostDTOPublic;
import hu.project.groupproject.dtos.postDTOs.out.PostDTOPublicExtended;
import hu.project.groupproject.entities.MyPost;
import hu.project.groupproject.services.PostService;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/post")
public class PostController {

    PostService postService;

    public PostController(PostService postService){
        this.postService=postService;
    }

    
    @PostMapping("/new")
    public boolean savePost(@RequestBody PostDTOCreate post){
        return postService.savePost(post);
    }

    @PutMapping("/{id}") //TODO:new dto to get user id, org id, post contents, image...  
    public boolean updatePost(@PathVariable Long id,@RequestBody PostDTOUpdate post){
        return postService.updatePost(id,post);
    }

    @GetMapping("/{id}/short")
    public Optional<PostDTOPublic> getPostShort(@PathVariable Long id) {
        return postService.getPostShort(id);
    }
    @GetMapping("/{id}/ex")
    public Optional<PostDTOPublicExtended> getPostEx(@PathVariable Long id) {
        return postService.getPostExtended(id);
    }
    
    @DeleteMapping("/del/id")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }
    
}
