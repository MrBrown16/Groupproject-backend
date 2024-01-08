package hu.project.groupproject.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.postDTOs.PostDTOPublic;
import hu.project.groupproject.dtos.postDTOs.PostDTOPublicExtended;
import hu.project.groupproject.dtos.voteDTOs.VoteDTOPublic;
import hu.project.groupproject.entities.MyPost;
import hu.project.groupproject.repositories.PostRepository;


@Service
public class PostService {
    
    PostRepository postRepository;
    VoteService voteService;

    
    public PostService(PostRepository postRepository, VoteService voteService){
        this.postRepository=postRepository;
        this.voteService=voteService;
    }
   
    public MyPost savePost(MyPost post){
        return postRepository.save(post);
    }
    public Optional<PostDTOPublic> getPostShort(Long id){
        return postRepository.findPostDtoById(id);
    }
    public Optional<PostDTOPublicExtended> getPostExtended(Long id){
        Optional<PostDTOPublic> post = postRepository.findPostDtoById(id);
        if (post.isPresent()) {
            Optional<VoteDTOPublic> vote = voteService.getVote(id);
            if (vote.isPresent()) {
                return Optional.of(new PostDTOPublicExtended(post.get(), vote.get()));
            }
        }
        return Optional.empty();
    }
    // public Optional<PostDTOPublic> getPost(Long id){
    //     return postRepository.findById(id, PostDTOPublic.class);
    // }

    public void deletePost(MyPost post){
        postRepository.delete(post);
    }




}
