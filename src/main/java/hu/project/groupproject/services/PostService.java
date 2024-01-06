package hu.project.groupproject.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.postDTOs.PostDTOPublic;
import hu.project.groupproject.entities.MyPost;
import hu.project.groupproject.repositories.PostRepository;


@Service
public class PostService {
    
    PostRepository postRepository;


    
    public PostService(PostRepository postRepository){
        this.postRepository=postRepository;
    }
   
    public MyPost savePost(MyPost post){
        return postRepository.save(post);
    }
    public Optional<PostDTOPublic> getPost(Long id){
        return postRepository.findPostDtoById(id);
    }
    // public Optional<PostDTOPublic> getPost(Long id){
    //     return postRepository.findById(id, PostDTOPublic.class);
    // }

    public void deletePost(MyPost post){
        postRepository.delete(post);
    }




}
