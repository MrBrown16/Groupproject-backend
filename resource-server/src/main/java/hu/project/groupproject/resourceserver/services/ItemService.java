package hu.project.groupproject.resourceserver.services;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.repositories.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ItemService {
    

    @PersistenceContext
    EntityManager manager;

    ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }



}
