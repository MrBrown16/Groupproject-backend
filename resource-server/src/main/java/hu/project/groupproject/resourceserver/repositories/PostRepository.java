package hu.project.groupproject.resourceserver.repositories;


import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial;
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
    Set<ItemDtoPublicPartial> findEventDtoByDescriptionLike(@Param(value = "search") String search);

    //date Update Before
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.updateTime <= :timestamp ")
    Set<PostDtoPublicNoImages> findPostDtoByUpdateTimeBefore(@Param(value = "timestamp") Timestamp timestamp);

    //date Update After
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.updateTime >= :timestamp ")
    Set<PostDtoPublicNoImages> findPostDtoByUpdateTimeAfter(@Param(value = "timestamp") Timestamp timestamp);

    //date Creation Before
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.creationTime <= :timestamp ")
    Set<PostDtoPublicNoImages> findPostDtoByCreationTimeBefore(@Param(value = "timestamp") Timestamp timestamp);

    //date Creation After
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicNoImages(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.content, p.likes, p.dislikes, p.vote.id) "+
    "FROM MyPost p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.creationTime >= :timestamp ")
    Set<PostDtoPublicNoImages> findPostDtoByCreationTimeAfter(@Param(value = "timestamp") Timestamp timestamp);
}
