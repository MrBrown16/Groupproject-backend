package hu.project.groupproject.resourceserver.repositories;


import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyPost;




public interface PostRepository extends JpaRepository<MyPost, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.id=:id")
    Optional<PostDtoPublicNoImages> findPostDtoById(@Param(value = "id") String id);

    //Done create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"

    //content
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Page<PostDtoPublicNoImages> findPostDtoByContentLike(@Param(value = "search") String search, Pageable pageable);

    //date Update Before
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.updateTime <= :timestamp ")
    Page<PostDtoPublicNoImages> findPostDtoByUpdateTimeBefore(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);

    //date Update After
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.updateTime >= :timestamp ")
    Page<PostDtoPublicNoImages> findPostDtoByUpdateTimeAfter(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);

    //date Creation Before
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.creationTime <= :timestamp ")
    Page<PostDtoPublicNoImages> findPostDtoByCreationTimeBefore(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);

    //date Creation After
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.creationTime >= :timestamp ")
    Page<PostDtoPublicNoImages> findPostDtoByCreationTimeAfter(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);
}
