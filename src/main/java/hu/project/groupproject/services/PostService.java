package hu.project.groupproject.services;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.postDTOs.in.PostDTOCreate;
import hu.project.groupproject.dtos.postDTOs.out.PostDTOPublic;
import hu.project.groupproject.dtos.postDTOs.out.PostDTOPublicExtended;
import hu.project.groupproject.dtos.voteDTOs.VoteDTOPublic;
import hu.project.groupproject.entities.MyOrg;
import hu.project.groupproject.entities.MyPost;
import hu.project.groupproject.entities.MyUser;
import hu.project.groupproject.entities.MyVote;
import hu.project.groupproject.entities.MyVoteOption;
import hu.project.groupproject.repositories.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class PostService {
    
    @PersistenceContext
    EntityManager entityManager;

    PostRepository postRepository;
    VoteService voteService;
    
    public PostService(PostRepository postRepository, VoteService voteService){
        this.postRepository=postRepository;
        this.voteService=voteService;
    }
   
    public MyPost updatePost(MyPost post){
        return postRepository.save(post);
    }

    public boolean savePost(PostDTOCreate postCreate){
        MyPost post = new MyPost();
        post.setUser(entityManager.getReference(MyUser.class, postCreate.userId()));
        post.setOrg(entityManager.getReference(MyOrg.class, postCreate.orgId()));
        post.setContent(postCreate.content());
        if (postCreate.optionTexts() != null && postCreate.voteDescription() != null && postCreate.optionTexts()!=new String[]{"",""} && postCreate.content()!="") {
            MyVote vote = new MyVote();
            List<MyVoteOption> options = new ArrayList<MyVoteOption>();
            int n = postCreate.optionTexts().length;
            for (int i=0; i<n; i++) {
                MyVoteOption option = new MyVoteOption();
                option.setOptionText(postCreate.optionTexts()[i]);
                option.setVote(vote);
                options.add(option);
            }
            vote.setDescription(postCreate.voteDescription());
            vote.setOptions(options);
            voteService.saveVote(vote);
            post.setVote(vote);
        }
        post = postRepository.save(post);
        
        System.out.println("--------------------------------------------------------"+post.getContent()+": "+post.getId()+"-----------------------------------------------------------------------------------");
        return true;
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
