package hu.project.groupproject.services;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.postDTOs.in.PostDTOCreate;
import hu.project.groupproject.dtos.postDTOs.in.PostDTOUpdate;
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
   
    public boolean updatePost(Long id, PostDTOUpdate postUpdate){
        if (id == postUpdate.id()) {
            Optional<MyPost> oldPost = postRepository.findById(id);
            if (oldPost.isPresent()) {
                MyPost post = oldPost.get();
                if (postUpdate.post().userId()==post.getUser().getId() && postUpdate.post().orgId()==post.getOrg().getId()) {
                    post.setContent(postUpdate.post().content());
                    if (post.getVote() != null ) {
                        MyVote vote = updateVoteInPost(postUpdate.post());
                        post.setVote(vote);
                    }
                    post = postRepository.save(post);
                    return true;
                }
            }
        }
        return false;
    }

    public MyVote updateVoteInPost(PostDTOCreate postCreate){
        if (postCreate.optionTexts() != null && postCreate.voteDescription() != null && postCreate.voteDescription() != "" && postCreate.optionTexts()!=new String[]{"",""}&& postCreate.optionTexts().length>=2 && postCreate.content()!="") {
            System.out.println("------------------------------------------------------------------  updateVoteInPost inside if  -----------------------------------------------------------------------------");
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
            return vote;
        }
        return null;
    }

    public boolean savePost(PostDTOCreate postCreate){
        MyPost post = new MyPost();
        if (postCreate.orgId() != null && postCreate.userId() != null) {
            
            post.setUser(entityManager.find(MyUser.class, postCreate.userId()));
            post.setOrg(entityManager.find(MyOrg.class, postCreate.orgId()));
            post.setContent(postCreate.content());
            MyVote vote = updateVoteInPost(postCreate);
            if (vote != null) {
                
                post.setVote(vote);
                post = postRepository.save(post);
            }
            
            return true;
        }
        return false;
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

    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }




}
