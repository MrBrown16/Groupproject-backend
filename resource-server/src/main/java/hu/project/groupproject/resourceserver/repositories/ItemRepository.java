package hu.project.groupproject.resourceserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyItemForSale;

public interface ItemRepository extends JpaRepository<MyItemForSale, String> {
    
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublic(i.id, i.user.id, i.name, i.description, i.state, i.location, i.phone) FROM MyItemForSale i LEFT JOIN i.user WHERE p.id=:id")
    Optional<ItemDtoPublicPartial> findItemDtoById(@Param(value = "id") String id);

    //TODO: create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"

}
