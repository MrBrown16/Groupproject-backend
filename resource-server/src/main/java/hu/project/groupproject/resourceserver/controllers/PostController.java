package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.posts.in.PostDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicWithImages;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicExtended;
import hu.project.groupproject.resourceserver.services.PostService;

import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
    public ImageUploadDetailsDto savePost(@RequestBody PostDtoCreate post){
        return postService.savePost(post);
    }

    @PutMapping("/{id}") 
    public ImageUploadDetailsDto updatePost(@PathVariable String id,@RequestBody PostDtoCreate post) throws NotFoundException{
        return postService.updatePost(id,post);
    }

    @GetMapping("/{id}/short")
    public Optional<PostDtoPublicWithImages> getPostShort(@PathVariable String id) {
        return postService.getPostShort(id);
    }

    @GetMapping("/{id}/ex")
    public Optional<PostDtoPublicExtended> getPostEx(@PathVariable String id) {
        return postService.getPostExtended(id);
    }
    
    @DeleteMapping("/del/id")//TODO: get userId from authentication
    public void deletePost(@PathVariable String postId) {
        postService.deletePost(null, postId);
    }
    
}
