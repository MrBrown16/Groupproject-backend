package hu.project.groupproject.resourceserver.services;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.En.posts.in.PostDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.posts.in.PostDtoUpdate;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicExtended;
import hu.project.groupproject.resourceserver.dtos.En.votes.VoteDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyPost;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyVote;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyVoteOption;
import hu.project.groupproject.resourceserver.repositories.PostRepository;
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
   
    public boolean updatePost(String id, PostDtoUpdate postUpdate){
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

    public MyVote updateVoteInPost(PostDtoCreate postCreate){
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

    public boolean savePost(PostDtoCreate postCreate){
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
    
    public Optional<PostDtoPublic> getPostShort(String id){
        return postRepository.findPostDtoById(id);
    }
    
    public Optional<PostDtoPublicExtended> getPostExtended(String id){
        Optional<PostDtoPublic> post = postRepository.findPostDtoById(id);
        if (post.isPresent()) {
            Optional<VoteDtoPublic> vote = voteService.getVote(id);
            if (vote.isPresent()) {
                return Optional.of(new PostDtoPublicExtended(post.get(), vote.get()));
            }
        }
        return Optional.empty();
    }

    public void deletePost(String postId){
        postRepository.deleteById(postId);
    }




}
