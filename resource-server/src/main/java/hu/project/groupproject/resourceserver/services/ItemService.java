package hu.project.groupproject.resourceserver.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.En.ItemDto;
import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicPartial;
import hu.project.groupproject.resourceserver.dtos.En.ItemDtoPublicWithImages;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyItemForSale;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ItemService {
    

    @PersistenceContext
    EntityManager manager;

    ItemRepository itemRepository;
    UserService userService;

    public ItemService(ItemRepository itemRepository, UserService userService) {
        this.itemRepository = itemRepository;
        this.userService= userService;
    }

    public void createItem(String userId,ItemDto itemDto){
        if (canEditItem(userId,null, itemDto)) {
            MyItemForSale item = new MyItemForSale();
            item = mapItemDtoToMyItemForSale(item, itemDto);
            manager.persist(item);
            
        }
    }
    public void updateItem(String userId,String itemId, ItemDto itemDto){
        if (canEditItem(userId, itemId, itemDto)) {
            MyItemForSale item = manager.find(MyItemForSale.class, itemId);
            item = mapItemDtoToMyItemForSale(item, itemDto);
            //should save by itself
        }
    }
    public void deleteItem(String userId,String itemId){
        if (canDeleteItem(userId, itemId)) {
            MyItemForSale item = manager.find(MyItemForSale.class, itemId);
            if (item != null) {
                itemRepository.delete(item);
            }
        }
    }

    public Optional<ItemDtoPublicWithImages> getItem(String itemId){
        return addImages(itemRepository.findItemDtoById(itemId));        
    }



    public Set<ItemDtoPublicWithImages> getItemsForUser(String userId){
        Set<String> itemIds = userService.getItemIdsForUser(userId);
        Set<ItemDtoPublicWithImages> items = new HashSet<>();
        for (String itemId : itemIds) {
            Optional<ItemDtoPublicWithImages> item = getItem(itemId);
            if (item.isPresent()) {
                items.add(item.get());
            }
        }
        return items;
        
    }

    private Optional<ItemDtoPublicWithImages> addImages(Optional<ItemDtoPublicPartial> noImageOpt){
        if (noImageOpt.isPresent()) {
            MyItemForSale item = manager.find(MyItemForSale.class, noImageOpt.get().itemId());
            if (item != null) {
                ItemDtoPublicPartial noImage = noImageOpt.get();
                return Optional.of(new ItemDtoPublicWithImages(noImage.itemId(), noImage.userId(), noImage.name(), noImage.description(), noImage.condition(), noImage.location(), noImage.phone(), item.getUrls()));
            }
        }
        return Optional.empty();
    }


    private boolean canEditItem(String userId,String itemId, ItemDto itemDto){
        if (userId != null && itemDto.userId() != null && userId == itemDto.userId()) {
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
        MyUser user = manager.find(MyUser.class, itemDto.userId());
        item.setUser(user);
        return item;
    }

}
