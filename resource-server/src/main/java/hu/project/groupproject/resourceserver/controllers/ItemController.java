package hu.project.groupproject.resourceserver.controllers;

import hu.project.groupproject.resourceserver.dtos.En.ItemDto;
import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicWithImages;
import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.ItemService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Set;


@RestController
@RequestMapping("/items")
public class ItemController {

    protected final Log logger = LogFactory.getLog(getClass());


    ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService=itemService;
    }
    
    @GetMapping("/page/{pageNum}")
    public Set<ItemDtoPublicWithImages> getItems(@PathVariable("pageNum") int page) {
        return itemService.getItems(page);
    }

    @GetMapping("/search/time")
    public Set<ItemDtoPublicWithImages> getPostsByTime(@RequestParam("time") Timestamp time, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
        return itemService.getItemsByTimeLike(time,pageNum,category);
    }
    @GetMapping("/search")
    public Set<ItemDtoPublicWithImages> searchPubUsersByProperty(@RequestParam("value") String value, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
        return itemService.getItemsByPropertyLike(pageNum, value, category);
    }
    
    @GetMapping("/{itemId}")
    public ItemDtoPublicWithImages getItem(@PathVariable String itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('USER')")
    public ImageUploadDetailsDto saveItem(@RequestBody ItemDto item, Authentication authentication){
        MyUser user = (MyUser)authentication.getPrincipal();
        return itemService.createItem(user.getId(),item);
    }

    @PutMapping("/{itemId}") 
    @PreAuthorize("hasRole('USER')")
    public ImageUploadDetailsDto updateItem(@PathVariable String itemId,@RequestBody ItemDto item, Authentication auth) throws NotFoundException{
        MyUser user = (MyUser)auth.getPrincipal();
        if (user.getId()==item.userId()) {
            return itemService.updateItem(user.getId(),itemId,item);
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
