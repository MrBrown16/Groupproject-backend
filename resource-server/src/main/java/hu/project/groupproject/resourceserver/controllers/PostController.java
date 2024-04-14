// package hu.project.groupproject.resourceserver.controllers;

// import org.springframework.web.bind.annotation.RestController;

// import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
// import hu.project.groupproject.resourceserver.dtos.En.posts.in.PostDtoCreate;
// import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
// import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicExtended;
// import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicWithImages;
// import hu.project.groupproject.resourceserver.services.PostService;

// import java.sql.Timestamp;
// import java.util.Optional;
// import java.util.Set;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
// import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;


// // not in use, news will be a separate thing i guess
// @RestController
// @RequestMapping("/post")
// public class PostController {

//     protected final Log logger = LogFactory.getLog(getClass());
    
//     PostService postService;
    
//     public PostController(PostService postService){
//         this.postService=postService;
//     }
    
//     // use /{id} getPostEx instead
//     // @GetMapping("/{id}/short")
//     // public Optional<PostDtoPublicWithImages> getPostShort(@PathVariable String id) {
//     //     return postService.getPostShort(id);
//     // }

//     @GetMapping("/{id}")
//     public Optional<PostDtoPublicExtended> getPostEx(@PathVariable String id) {
//         return postService.getPostExtended(id);
//     }
//     @GetMapping("/search/content")
//     public Set<PostDtoPublicWithImages> getPostsByContentLike(@RequestParam("value") String value, @RequestParam("pageNum") int pageNum) {
//         return postService.getPostsByContentLike(value,pageNum);
//     }
//     @GetMapping("/search/time")
//     public Set<PostDtoPublicWithImages> getPostsByTime(@RequestParam("time") Timestamp time, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
//         return postService.getPostsByTimeLike(time,pageNum,category);
//     }
    
//     @PostMapping("/new")
//     @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
//     public ImageUploadDetailsDto savePost(@RequestBody PostDtoCreate post){
//         return postService.createPost(post);
//     }

//     @PutMapping("/{id}") 
//     @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
//     public ImageUploadDetailsDto updatePost(@PathVariable("id") String id, @RequestBody PostDtoCreate post, Authentication auth) throws NotFoundException{
//         MyUser user = (MyUser)auth.getPrincipal();
//         if (user.getId()==post.userId() && id != null) {
//             return postService.updatePost(id,post);
//         }
//         throw new AccessDeniedException("You don't have the right to change this post");
//     }

    
//     @DeleteMapping("/del/{postId}")
//     @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
//     public void deletePost(@PathVariable String postId, Authentication auth) {
//         MyUser user = (MyUser)auth.getPrincipal();
//         logger.debug(user.getId());
//         postService.deletePost(user.getId(), postId);
//     }
    
// }
