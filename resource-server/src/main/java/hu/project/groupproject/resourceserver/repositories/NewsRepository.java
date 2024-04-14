package hu.project.groupproject.resourceserver.repositories;


import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNews;




public interface NewsRepository extends JpaRepository<MyNews, String>{
    
    <T> Optional<T> findById(String id, Class<T> type);

    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.title, p.content, p.type) "+
    "FROM MyNews p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.id=:id")
    Optional<NewsDtoPublic> findNewsDtoById(@Param(value = "id") String id);

    //Done create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"
    
    //title
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.title, p.content, p.type) "+
    "FROM MyNews p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.title LIKE UPPER(CONCAT('%', :search, '%')) ")    
    Page<NewsDtoPublic> findNewsDtoByTitleLike(@Param(value = "search") String search, Pageable pageable);

    //content
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.title, p.content, p.type) "+
    "FROM MyNews p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.content LIKE UPPER(CONCAT('%', :search, '%')) ")    
    Page<NewsDtoPublic> findNewsDtoByContentLike(@Param(value = "search") String search, Pageable pageable);

    //type
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.title, p.content, p.type) "+
    "FROM MyNews p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.type LIKE UPPER(CONCAT('%', :search, '%')) ")    
    Page<NewsDtoPublic> findNewsDtoByTypeLike(@Param(value = "search") String search, Pageable pageable);

    //date Update Before
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.title, p.content, p.type) "+
    "FROM MyNews p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.updateTime <= :timestamp ")
    Page<NewsDtoPublic> findNewsDtoByUpdateTimeBefore(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);

    //date Update After
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.title, p.content, p.type) "+
    "FROM MyNews p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.updateTime >= :timestamp ")
    Page<NewsDtoPublic> findNewsDtoByUpdateTimeAfter(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);

    //date Creation Before
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.title, p.content, p.type) "+
    "FROM MyNews p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.creationTime <= :timestamp ")
    Page<NewsDtoPublic> findNewsDtoByCreationTimeBefore(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);

    //date Creation After
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic(p.id, p.user.id, p.user.userName, p.org.id, p.org.name, p.title, p.content, p.type) "+
    "FROM MyNews p LEFT JOIN p.user LEFT JOIN p.org "+
    "WHERE p.creationTime >= :timestamp ")
    Page<NewsDtoPublic> findNewsDtoByCreationTimeAfter(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);
}
