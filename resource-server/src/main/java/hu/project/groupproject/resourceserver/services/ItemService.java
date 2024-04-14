package hu.project.groupproject.resourceserver.services;

import hu.project.groupproject.resourceserver.dtos.En.ItemDto;
import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial;
import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicWithImages;
import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyItemForSale;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ItemService {
    
    protected final Log logger = LogFactory.getLog(getClass());


    @PersistenceContext
    EntityManager manager;

    ItemRepository itemRepository;
    UserService userService;

    public ItemService(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService= userService;
    }

    public Set<ItemDtoPublicWithImages> getItems(int pageNum){
        Page<MyItemForSale> items =  itemRepository.findAll(Pageable.ofSize(10).withPage(pageNum));
        Set<ItemDtoPublicWithImages> images = new HashSet<>();
        items.forEach((item)->{
            images.add(addImagesToItem(item));
        });
        return images;
    }
    
    @Transactional
    public ImageUploadDetailsDto createItem(String userId,ItemDto itemDto){
        if (canEditItem(userId,null, itemDto)) {
            MyItemForSale item = new MyItemForSale();
            item = mapItemDtoToMyItemForSale(item, itemDto);
            manager.persist(item);
            return new ImageUploadDetailsDto("images/"+item.getPath(), true);
        }
        throw new AccessDeniedException("You don't have the right to change this item");

    }
    public ImageUploadDetailsDto updateItem(String userId,String itemId, ItemDto itemDto){
        if (canEditItem(userId, itemId, itemDto)) {
            MyItemForSale item = manager.find(MyItemForSale.class, itemId);
            item = mapItemDtoToMyItemForSale(item, itemDto);
            //should save by itself
            return new ImageUploadDetailsDto("images/"+item.getPath(), true);
        }
        throw new AccessDeniedException("You don't have the right to change this item");

    }
    public ImageUploadDetailsDto updateItem(Authentication auth,String itemId, ItemDto itemDto){
        MyItemForSale item = manager.find(MyItemForSale.class, itemId);
        if (canEditItem(auth, item, itemDto)) {
            item = mapItemDtoToMyItemForSale(item, itemDto);
            //should save by itself
            return new ImageUploadDetailsDto("images/"+item.getPath(), true);
        }
        throw new AccessDeniedException("You don't have the right to change this item");

    }
    public void deleteItem(String userId,String itemId){
        if (canDeleteItem(userId, itemId)) {
            MyItemForSale item = manager.find(MyItemForSale.class, itemId);
            if (item != null) {
                itemRepository.delete(item);
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You don't have the right to delete this item");
    }

    public ItemDtoPublicWithImages getItem(String itemId){
        Optional<ItemDtoPublicPartial> item = itemRepository.findItemDtoById(itemId);
        if(item.isPresent()){
            return addImagesToItem(item.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No item found by provided id");
    }
    private Optional<ItemDtoPublicWithImages> getItemOpt(String itemId){
        return addImagesOpt(itemRepository.findItemDtoById(itemId));
    }
    
    
    public Set<ItemDtoPublicWithImages> getItemsForUser(String userId){
        Set<String> itemIds = userService.getItemIdsForUser(userId);
        Set<ItemDtoPublicWithImages> items = new HashSet<>();
        for (String itemId : itemIds) {
            Optional<ItemDtoPublicWithImages> item = getItemOpt(itemId);
            if (item.isPresent()) {
                items.add(item.get());
            }
        }
        return items;
        
    }

    public Set<ItemDtoPublicWithImages> getItemsByTimeLike(Timestamp time, int pageNum, String category){
        Page<MyItemForSale> items;
        switch (category) {
            case "updateBefore":
                items = itemRepository.findItemDtoByUpdateTimeBefore(time, Pageable.ofSize(10).withPage(pageNum));
                
                break;
            case "updateAfter":
                items = itemRepository.findItemDtoByUpdateTimeAfter(time, Pageable.ofSize(10).withPage(pageNum));
                
                break;
            case "createBefore":
                items = itemRepository.findItemDtoByCreationTimeBefore(time, Pageable.ofSize(10).withPage(pageNum));
                
                break;
            case "createAfter":
                items = itemRepository.findItemDtoByCreationTimeAfter(time, Pageable.ofSize(10).withPage(pageNum));
                
                break;
        
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Set<ItemDtoPublicWithImages> images = new HashSet<>();
        for (MyItemForSale item : items) {
            images.add(addImagesToItem(item));
        }
        return images;
    }
    public Set<ItemDtoPublicWithImages> getItemsByPriceLike(Long price, int pageNum, String category){
        Page<MyItemForSale> items;
        switch (category) {
            case "lower":
                items = itemRepository.findItemDtoByPriceLower(price, Pageable.ofSize(10).withPage(pageNum));
                
                break;
            case "higher":
                items = itemRepository.findItemDtoByPriceHigher(price, Pageable.ofSize(10).withPage(pageNum));
                
                break;       
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Set<ItemDtoPublicWithImages> images = new HashSet<>();
        for (MyItemForSale item : items) {
            images.add(addImagesToItem(item));
        }
        return images;
    }

    public Set<ItemDtoPublicWithImages> getItemsByPropertyLike(String value, int pageNum, String property){
        Page<MyItemForSale> items;
        switch (property) {
            case "name":
                items = itemRepository.findItemDtoByNameLike(value, Pageable.ofSize(10).withPage(pageNum));

                break;
            case "description":
                items = itemRepository.findItemDtoByDescriptionLike(value, Pageable.ofSize(10).withPage(pageNum));
            
                break;
            case "location":
                items = itemRepository.findItemDtoByLocationLike(value, Pageable.ofSize(10).withPage(pageNum));
                
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                
        }
        Set<ItemDtoPublicWithImages> images = new HashSet<>();
        for (MyItemForSale item : items) {
            images.add(addImagesToItem(item));
        }
        return images;
    }


    private Optional<ItemDtoPublicWithImages> addImagesOpt(Optional<ItemDtoPublicPartial> noImageOpt){
        if (noImageOpt.isPresent()) {
            MyItemForSale item = manager.find(MyItemForSale.class, noImageOpt.get().itemId());
            if (item != null) {
                ItemDtoPublicPartial noImage = noImageOpt.get();
                return Optional.of(new ItemDtoPublicWithImages(noImage.itemId(), noImage.userId(), noImage.name(), noImage.description(), noImage.condition(), noImage.location(), noImage.email(), noImage.phone(), noImage.price(), item.getUrls()));
            }
        }
        return Optional.empty();
    }
    private ItemDtoPublicWithImages addImagesToItem(MyItemForSale noImage){
        return new ItemDtoPublicWithImages(noImage.getId(), noImage.getUser().getId(), noImage.getName(), noImage.getDescription(), noImage.getCondition(), noImage.getLocation(),noImage.getEmail(), noImage.getPhone(),noImage.getPrice(), noImage.getUrls());
    }
    private ItemDtoPublicWithImages addImagesToItem(ItemDtoPublicPartial noImage){
        MyItemForSale item = manager.find(MyItemForSale.class,noImage.itemId());
        return new ItemDtoPublicWithImages(noImage.itemId(), noImage.userId(), noImage.name(), noImage.description(), noImage.condition(), noImage.location(), noImage.email(), noImage.phone(),noImage.price(), item.getUrls());
    }


    private boolean canEditItem(String userId,String itemId, ItemDto itemDto){
        // if (userId!=null) {
        //     System.out.println("userId!=null");
        // }
        // if (itemDto.userId()!=null) {
        //     System.out.println("itemDto.userId()!=null");
        // }
        // if (userId.equals(itemDto.userId())) {
        //     System.out.println("userId!=itemDto.userId()"+userId+", "+itemDto.userId());
        // }
        if (userId != null && itemDto.userId() != null && userId.equals(itemDto.userId())) {
            MyUser user = manager.find(MyUser.class, userId);
            if (user != null) {
                if (itemId != null) {
                    MyItemForSale item = manager.find(MyItemForSale.class, itemId);
                    if (item != null && item.getUser()==user) {
                        return true;
                    }
                }else{
                    return true;
                }
            }
        }
        return false;
    }
    private boolean canEditItem(Authentication auth,MyItemForSale item, ItemDto itemDto){
        // if (auth.getAuthorities().contains("ROLE_ADMIN")) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            //TODO:check if it works
            logger.debug("admin so allowed");
            return true;
        }
        MyUser user = (MyUser)auth;
            if (user != null && item != null && itemDto != null 
                    && item.getUser().getId()==user.getId() && itemDto.itemId() == item.getId()) {
                        return true;
            }
        return false;
    }

    private boolean canDeleteItem(String userId,String itemId){
        if (userId != null && itemId != null) {
            MyUser user = manager.find(MyUser.class, userId);
            MyItemForSale item = manager.find(MyItemForSale.class, itemId);
            if (item != null && user != null && item.getUser() == user) {
                return true;
            }
        }
        return false;
    }

    private MyItemForSale mapItemDtoToMyItemForSale(MyItemForSale item, ItemDto itemDto){
        item.setCondition(itemDto.condition());
        item.setDescription(itemDto.description());
        item.setLocation(itemDto.location());
        item.setName(itemDto.name());
        item.setPhone(itemDto.phone());
        item.setEmail(itemDto.email());
        item.setPrice(itemDto.price());
        MyUser user = manager.find(MyUser.class, itemDto.userId());
        item.setUser(user);
        return item;
    }

}
