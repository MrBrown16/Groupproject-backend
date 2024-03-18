package hu.project.groupproject.resourceserver.repositories;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyItemForSale;

public interface ItemRepository extends JpaRepository<MyItemForSale, String> {
    
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) FROM MyItemForSale i LEFT JOIN i.user WHERE p.id=:id")
    Optional<ItemDtoPublicPartial> findItemDtoById(@Param(value = "id") String id);

    //Done create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"

    //name
    @Query("SELECT i "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<MyItemForSale> findItemDtoByNameLike(@Param(value = "name") String name, Pageable pageable);
    
    //description
    @Query("SELECT i "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Page<MyItemForSale> findItemDtoByDescriptionLike(@Param(value = "search") String search, Pageable pageable);
    
    //location
    @Query("SELECT i "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE LOWER(i.location) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Page<MyItemForSale> findItemDtoByLocationLike(@Param(value = "search") String search, Pageable pageable);
    
    //date Update Before
    @Query("SELECT i "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE i.updateTime <= :timestamp ")
    Page<MyItemForSale> findItemDtoByUpdateTimeBefore(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);
    
    //date Update After
    @Query("SELECT i "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE i.updateTime >= :timestamp ")
    Page<MyItemForSale> findItemDtoByUpdateTimeAfter(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);

    //date Update Before
    @Query("SELECT i "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE i.creationTime <= :timestamp ")
    Page<MyItemForSale> findItemDtoByCreationTimeBefore(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);
    
    //date Update After
    @Query("SELECT i "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE i.creationTime >= :timestamp ")
    Page<MyItemForSale> findItemDtoByCreationTimeAfter(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);
}
