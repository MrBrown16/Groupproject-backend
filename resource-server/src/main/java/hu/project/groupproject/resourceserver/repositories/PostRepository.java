package hu.project.groupproject.resourceserver.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyPost;




public interface PostRepository extends JpaRepository<MyPost, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org WHERE p.id=:id")
    Optional<PostDtoPublicNoImages> findPostDtoById(@Param(value = "id") String id);

    
}
