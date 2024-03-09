package hu.project.groupproject.resourceserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyItemForSale;

public interface ItemRepository extends JpaRepository<MyItemForSale, String> {
    
}
