package hu.project.groupproject.resourceserver.repositories;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

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
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Set<ItemDtoPublicPartial> findEventDtoByNameLike(@Param(value = "name") String name);
    
    //description
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Set<ItemDtoPublicPartial> findEventDtoByDescriptionLike(@Param(value = "search") String search);
    
    //location
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE LOWER(i.location) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Set<ItemDtoPublicPartial> findEventDtoByLocationLike(@Param(value = "search") String search);
    
    //date Update Before
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE i.updateTime <= :timestamp ")
    Set<ItemDtoPublicPartial> findItemDtoByUpdateTimeBefore(@Param(value = "timestamp") Timestamp timestamp);
    
    //date Update After
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE i.updateTime >= :timestamp ")
    Set<ItemDtoPublicPartial> findItemDtoByUpdateTimeAfter(@Param(value = "timestamp") Timestamp timestamp);

    //date Update Before
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE i.creationTime <= :timestamp ")
    Set<ItemDtoPublicPartial> findItemDtoByCreationTimeBefore(@Param(value = "timestamp") Timestamp timestamp);
    
    //date Update After
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) "+
    "FROM MyItemForSale i LEFT JOIN i.user"+
    "WHERE i.creationTime >= :timestamp ")
    Set<ItemDtoPublicPartial> findItemDtoByCreationTimeAfter(@Param(value = "timestamp") Timestamp timestamp);
}
