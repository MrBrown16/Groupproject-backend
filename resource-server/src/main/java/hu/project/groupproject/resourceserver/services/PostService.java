package hu.project.groupproject.resourceserver.services;

import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.posts.in.PostDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicWithImages;
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
    EntityManager manager;

    PostRepository postRepository;
    VoteService voteService;
    UserService userService;
    
    public PostService(PostRepository postRepository, VoteService voteService, UserService userService){
        this.postRepository=postRepository;
        this.voteService=voteService;
        this.userService=userService;
    }
   
    @SuppressWarnings("null")
    public ImageUploadDetailsDto updatePost(String postId, PostDtoCreate postUpdate) throws NotFoundException{
            Optional<MyPost> oldPost = postRepository.findById(postId);
            if (oldPost.isPresent()) {
                MyPost post = oldPost.get();
                if (canEditPost(postId, postUpdate)) {
                    post.setContent(postUpdate.content());
                    if (post.getVote() != null ) {
                        MyVote vote = updateVoteInPost(postUpdate);
                        post.setVote(vote);
                    }
                    post = postRepository.save(post);
                    return new ImageUploadDetailsDto("images/"+post.getPath(), true);
                }
                throw new AccessDeniedException("You don't have the right to change this post");
            }
        throw new NotFoundException();
    }

    
    public ImageUploadDetailsDto createPost(PostDtoCreate postCreate){
        MyPost post = new MyPost();
        if (canEditPost(null, postCreate)) {
            
            post.setUser(manager.find(MyUser.class, postCreate.userId()));
            post.setOrg(manager.find(MyOrg.class, postCreate.orgId()));
            post.setContent(postCreate.content());
            MyVote vote = updateVoteInPost(postCreate);
            if (vote != null) {
                
                post.setVote(vote);
            }
            post = postRepository.save(post);
            return new ImageUploadDetailsDto("images/"+post.getPath(), true);
        }
        throw new AccessDeniedException("You don't have the right to change this post");
    }
    
    public Optional<PostDtoPublicWithImages> getPostShort(String id){

        return addImages(postRepository.findPostDtoById(id));
    }
    
    public Set<PostDtoPublicExtended> getPostsForUser(String userId){
        Set<String> postIds = userService.getPostsIdsForUser(userId);
        Set<PostDtoPublicExtended> posts = new HashSet<>();
        for (String postId : postIds) {
            Optional<PostDtoPublicExtended> post = getPostExtended(postId);
            if (post.isPresent()) {
                posts.add(post.get());
            }
        }
        return posts;
        
    }

    public Set<PostDtoPublicWithImages> getPostsByContentLike(String value, int pageNum){
        Page<PostDtoPublicNoImages> posts = postRepository.findPostDtoByContentLike(value, Pageable.ofSize(10).withPage(pageNum));
        Set<PostDtoPublicWithImages> images = new HashSet<>();
        for (PostDtoPublicNoImages post : posts) {
            images.add(addImages(post));
        }
        return images;
    }
    public Set<PostDtoPublicWithImages> getPostsByTimeLike(Timestamp time, int pageNum, String category){
        Page<PostDtoPublicNoImages> posts;
        switch (category) {
            case "updateBefore":
                posts = postRepository.findPostDtoByUpdateTimeBefore(time, Pageable.ofSize(10).withPage(pageNum));
                
                break;
            case "updateAfter":
                posts = postRepository.findPostDtoByUpdateTimeAfter(time, Pageable.ofSize(10).withPage(pageNum));
                
                break;
            case "createBefore":
                posts = postRepository.findPostDtoByCreationTimeBefore(time, Pageable.ofSize(10).withPage(pageNum));
                
                break;
            case "createAfter":
                posts = postRepository.findPostDtoByCreationTimeAfter(time, Pageable.ofSize(10).withPage(pageNum));
                
                break;
        
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Set<PostDtoPublicWithImages> images = new HashSet<>();
        for (PostDtoPublicNoImages post : posts) {
            images.add(addImages(post));
        }
        return images;
    }
    
    public Optional<PostDtoPublicExtended> getPostExtended(String id){
        Optional<PostDtoPublicWithImages> post = addImages(postRepository.findPostDtoById(id));
        if (post.isPresent()) {
            Optional<VoteDtoPublic> vote = voteService.getVote(id);
            if (vote.isPresent()) {
                return Optional.of(new PostDtoPublicExtended(post.get(), vote.get()));
            }
        }
        return Optional.empty();
    }
    
    @SuppressWarnings("null")
    public void deletePost(String userId, String postId){
        if (canDeletePost(userId, postId)) {
            postRepository.deleteById(postId);
        }
    }

    private boolean canEditPost(String postId, PostDtoCreate post){
        if (post.userId() != null && post.orgId() != null) {
            MyUser user =manager.find(MyUser.class, post.userId());
            if (user != null) {
                MyOrg org = manager.find(MyOrg.class, post.orgId());
                if (org != null && user.getOrgs().contains(org) && org.getUsers().contains(user)) {
                    if (postId != null) {
                        MyPost myPost = manager.find(MyPost.class, postId);
                        if (myPost.getOrg().getId().equals(post.orgId())) {
                            return true;
                        }
                        return false;
                    }else{
                        return true;
                    }
                }
            }
        }else if (post.userId() != null) {
            MyUser user =manager.find(MyUser.class, post.userId());
            if (user != null) {
                if (postId != null) {
                    MyPost myPost = manager.find(MyPost.class, postId);
                    if (myPost != null && myPost.getUser().getId().equals(user.getId())) {
                        return true;
                    }
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canDeletePost(String userId, String postId){
        if (postId != null && userId != null) {
            MyUser user =manager.find(MyUser.class, userId);
            if (user != null) {
                MyPost myPost = manager.find(MyPost.class, postId);
                if (myPost != null && user.getOrgs().contains(myPost.getOrg())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    private Optional<PostDtoPublicWithImages> addImages(Optional<PostDtoPublicNoImages> optNoImages){
        PostDtoPublicWithImages withImages;
        if (!optNoImages.isPresent()) {
            return Optional.empty();
        }
        PostDtoPublicNoImages noImages = optNoImages.get();
        MyPost post = manager.find(MyPost.class, noImages.id());
        if (post != null) {
            withImages = new PostDtoPublicWithImages(noImages.id(), noImages.userId(), noImages.userName(), noImages.orgId(), noImages.orgname(), noImages.content(), post.getUrls(), noImages.likes(), noImages.dislikes(), noImages.voteId());
        }
        withImages = new PostDtoPublicWithImages(noImages.id(), noImages.userId(), noImages.userName(), noImages.orgId(), noImages.orgname(), noImages.content(), new String[0], noImages.likes(), noImages.dislikes(), noImages.voteId());
        
        return Optional.of(withImages);
    }
    private PostDtoPublicWithImages addImages(PostDtoPublicNoImages noImages){
        MyPost post = manager.find(MyPost.class, noImages.id());
        return new PostDtoPublicWithImages(noImages.id(), noImages.userId(), noImages.userName(), noImages.orgId(), noImages.orgname(), noImages.content(), post.getUrls(), noImages.likes(), noImages.dislikes(), noImages.voteId());      
    }


    private MyVote updateVoteInPost(PostDtoCreate postCreate){
        if (postCreate.optionTexts() != null && postCreate.voteDescription() != null && postCreate.voteDescription() != "" && postCreate.optionTexts()!=new String[]{"",""}&& postCreate.optionTexts().length>=2 && postCreate.content()!="") {
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


}
