package hu.project.groupproject.resourceserver.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.En.EventDto;
import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.posts.out.PostDtoPublicExtended;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyEvent;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class EventService {
    
    @PersistenceContext
    EntityManager manager;

    EventRepository eventRepository;

    OrgService orgService;

    public EventService(EventRepository eventRepository, OrgService orgService) {
        this.eventRepository = eventRepository;
        this.orgService=orgService;
    }
    
    public void createEvent(String userId,EventDto eventDto){
        if (canEditEvent(userId,null, eventDto)) {
            MyEvent event = new MyEvent();
            event = mapEventDtoToMyEvent(event, eventDto);
            manager.persist(event);
            
        }
    }
    public void updateEvent(String userId,String eventId, EventDto eventDto){
        if (canEditEvent(userId, eventId, eventDto)) {
            MyEvent event = manager.find(MyEvent.class, eventId);
            event = mapEventDtoToMyEvent(event, eventDto);
            //should save by itself
        }
    }
    public void deleteEvent(String userId,String eventId){
        if (canDeleteEvent(userId, eventId)) {
            MyEvent event = manager.find(MyEvent.class, eventId);
            if (event != null) {
                eventRepository.delete(event);
            }
        }
    }

    public Optional<EventDtoPublic> getEvent(String eventId){
        return eventRepository.findEventDtoById(eventId);        
    }



    public Set<EventDtoPublic> getEventsForOrg(String orgId){
        Set<String> eventIds = orgService.getEventsIdsForOrg(orgId);
        Set<EventDtoPublic> events = new HashSet<>();
        for (String eventId : eventIds) {
            Optional<EventDtoPublic> event = getEvent(eventId);
            if (event.isPresent()) {
                events.add(event.get());
            }
        }
        return events;
        
    }










    private boolean canEditEvent(String userId,String eventId, EventDto eventDto){
        if (userId != null && eventDto.userId() != null && userId == eventDto.userId() && eventDto.orgId() != null) {
            MyUser user = manager.find(MyUser.class, userId);
            MyOrg org = manager.find(MyOrg.class, eventDto.orgId());
            if (user != null && org != null && user.getOrgs().contains(org)) {
                if (eventId != null) {
                    MyEvent event = manager.find(MyEvent.class, eventId);
                    if (event != null && event.getOrganiser()==org) {
                        return true;
                    }
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canDeleteEvent(String userId,String eventId){
        if (userId != null && eventId != null) {
            MyUser user = manager.find(MyUser.class, userId);
            MyEvent event = manager.find(MyEvent.class, eventId);
            if (event != null && user != null && user.getOrgs().contains(event.getOrganiser())) {
                return true;
            }
        }
        return false;
    }

    private MyEvent mapEventDtoToMyEvent(MyEvent event, EventDto eventDto){
        event.setDescription(eventDto.description());
        event.setName(eventDto.name());
        event.setLocation(eventDto.location());
        MyOrg org = manager.find(MyOrg.class, eventDto.orgId());
        event.setOrganiser(org);
        MyUser user = manager.find(MyUser.class, eventDto.userId());
        event.setOrganiserUser(user);
        event.setStartDate(eventDto.startDate());
        event.setEndDate(eventDto.endDate());
        event.setPublicEmails(eventDto.publicEmails());
        event.setPublicPhones(eventDto.publicPhones());
        return event;
    }
}
