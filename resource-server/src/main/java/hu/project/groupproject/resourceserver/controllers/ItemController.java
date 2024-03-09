package hu.project.groupproject.resourceserver.controllers;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.ItemDto;
import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicWithImages;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.ItemService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/items")
public class ItemController {

    protected final Log logger = LogFactory.getLog(getClass());

    @PersistenceContext
    EntityManager manager;

    ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService=itemService;
    }
    
    @GetMapping("/{itemId}")
    public Optional<ItemDtoPublicWithImages> getItem(@PathVariable String itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('USER')")
    public void saveItem(@RequestBody ItemDto item, Authentication authentication){
        MyUser user = (MyUser)authentication.getPrincipal();
        itemService.createItem(user.getId(),item);
    }

    @PutMapping("/{itemId}") 
    @PreAuthorize("hasRole('USER')")
    public void updateItem(@PathVariable String itemId,@RequestBody ItemDto item, Authentication auth) throws NotFoundException{
        MyUser user = (MyUser)auth.getPrincipal();
        if (user.getId()==item.userId()) {
            itemService.updateItem(user.getId(),itemId,item);
        }
        throw new AccessDeniedException("You don't have the right to change this item");
    }
    
    @DeleteMapping("/del/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteItem(@PathVariable String itemId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        itemService.deleteItem(user.getId(), itemId);
    }
}
