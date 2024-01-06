package hu.project.groupproject.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.dtos.postDTOs.PostDTOPublic;
import hu.project.groupproject.entities.MyPost;



public interface PostRepository extends JpaRepository<MyPost, Long>{
    
    <T> Optional<T> findById(Long id, Class<T> type);

    @Query("SELECT new hu.project.groupproject.dtos.postDTOs.PostDTOPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.url, p.imagePath, p.likes, p.dislikes, p.vote.id) FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org WHERE p.id=:id")
    Optional<PostDTOPublic> findPostDtoById(@Param(value = "id") Long id);

}
