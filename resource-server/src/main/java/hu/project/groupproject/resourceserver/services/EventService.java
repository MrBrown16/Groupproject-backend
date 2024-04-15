package hu.project.groupproject.resourceserver.services;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.En.EventDto;
import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyEvent;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class EventService {
    
    protected final Log logger = LogFactory.getLog(getClass());


    @PersistenceContext
    EntityManager manager;

    EventRepository eventRepository;

    OrgService orgService;

    public EventService(EventRepository eventRepository, OrgService orgService) {
        this.eventRepository = eventRepository;
        this.orgService=orgService;
    }
    
    public void createEvent(EventDto eventDto,Authentication auth){
        if (canEditEvent(null, eventDto,auth)) {
            MyEvent event = new MyEvent();
            event = mapEventDtoToMyEvent(event, eventDto);
            manager.persist(event);
            
        }
    }
    @Transactional
    public void updateEvent(String eventId, EventDto eventDto, Authentication auth){
        if (canEditEvent(eventId, eventDto,auth)) {
            MyEvent event = manager.find(MyEvent.class, eventId);
            event = mapEventDtoToMyEvent(event, eventDto);
            //should save by itself
            manager.persist(event);
        }
    }
    public void deleteEvent(String userId,String eventId, Authentication auth){
        MyUser user = manager.find(MyUser.class, userId);
        MyEvent event = manager.find(MyEvent.class, eventId);
        if (canDeleteEvent(user, event, auth)) {
            eventRepository.delete(event);
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

    public Page<EventDtoPublic> getEventsByPropertyLike(int pageNum, String value, String property){
        switch (property) {
            case "name":
                return eventRepository.findEventDtoByNameLike(value, Pageable.ofSize(10).withPage(pageNum));
            case "description":
                return eventRepository.findEventDtoByDescriptionLike(value, Pageable.ofSize(10).withPage(pageNum));
            case "location":
                return eventRepository.findEventDtoByLocationLike(value, Pageable.ofSize(10).withPage(pageNum));
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                
        }
    }

    public Page<EventDtoPublic> getEventsHappeningAtTime(Timestamp time, int pageNum){
        return eventRepository.findEventDtoByDateBetweenTruncatedToDay(time, Pageable.ofSize(10).withPage(pageNum));
    }



    private boolean canEditEvent(String eventId, EventDto eventDto,Authentication auth){
        logger.debug("canEditEvent authorities: "+auth.getAuthorities());
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        }
        MyUser userr = (MyUser)auth.getPrincipal();
        String userId = userr.getId();
        if (userId != null && eventDto.userId() != null && userId.equals(eventDto.userId()) && eventDto.orgId() != null) {
            MyUser user = manager.find(MyUser.class, userId);
            MyOrg org = manager.find(MyOrg.class, eventDto.orgId());
            if (user != null && org != null && user.getOrgs().contains(org)) {
                if (eventId != null) {
                    MyEvent event = manager.find(MyEvent.class, eventId);
                    if (event != null && event.getOrganiser().equals(org)) {
                        return true;
                    }
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canDeleteEvent(String userId,String eventId, Authentication auth){
        if (userId != null && eventId != null) {
            MyUser user = manager.find(MyUser.class, userId);
            MyEvent event = manager.find(MyEvent.class, eventId);
            if (event != null && user != null && user.getOrgs().contains(event.getOrganiser())) {
                return true;
            }
        }
        return false;
    }
    private boolean canDeleteEvent(MyUser user,MyEvent event, Authentication auth){
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {//TODO: check if this checks admin role correctly
            logger.debug("canDeleteEvent auth.getAuthorities().contains(new SimpleGrantedAuthority(\"ROLE_ADMIN\")");
            return true;
        }
        if (event != null && user != null && user.getOrgs().contains(event.getOrganiser())) {
            return true;
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
// package hu.project.groupproject.resourceserver.services;

// import java.sql.Timestamp;
// import java.util.HashSet;
// import java.util.Optional;
// import java.util.Set;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Service;
// import org.springframework.web.server.ResponseStatusException;

// import hu.project.groupproject.resourceserver.dtos.En.EventDto;
// import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
// import hu.project.groupproject.resourceserver.entities.softdeletable.MyEvent;
// import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
// import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
// import hu.project.groupproject.resourceserver.repositories.EventRepository;
// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;

// @Service
// public class EventService {
    
//     @PersistenceContext
//     EntityManager manager;

//     EventRepository eventRepository;

//     OrgService orgService;

//     public EventService(EventRepository eventRepository, OrgService orgService) {
//         this.eventRepository = eventRepository;
//         this.orgService=orgService;
//     }
    
//     public void createEvent(String userId,EventDto eventDto){
//         if (canEditEvent(userId,null, eventDto)) {
//             MyEvent event = new MyEvent();
//             event = mapEventDtoToMyEvent(event, eventDto);
//             manager.persist(event);
            
//         }
//     }
        // @Transactional
//     public void updateEvent(String userId,String eventId, EventDto eventDto){
//         if (canEditEvent(userId, eventId, eventDto)) {
//             MyEvent event = manager.find(MyEvent.class, eventId);
//             event = mapEventDtoToMyEvent(event, eventDto);
//             //should save by itself
                // manager.persist(event);
//         }
//     }
//     public void deleteEvent(String userId,String eventId){
//         if (canDeleteEvent(userId, eventId)) {
//             MyEvent event = manager.find(MyEvent.class, eventId);
//             if (event != null) {
//                 eventRepository.delete(event);
//             }
//         }
//     }

//     public Optional<EventDtoPublic> getEvent(String eventId){
//         return eventRepository.findEventDtoById(eventId);        
//     }



//     public Set<EventDtoPublic> getEventsForOrg(String orgId){
//         Set<String> eventIds = orgService.getEventsIdsForOrg(orgId);
//         Set<EventDtoPublic> events = new HashSet<>();
//         for (String eventId : eventIds) {
//             Optional<EventDtoPublic> event = getEvent(eventId);
//             if (event.isPresent()) {
//                 events.add(event.get());
//             }
//         }
//         return events;
        
//     }

//     public Page<EventDtoPublic> getEventsByPropertyLike(int pageNum, String value, String property){
//         switch (property) {
//             case "name":
//                 return eventRepository.findEventDtoByNameLike(value, Pageable.ofSize(10).withPage(pageNum));
//             case "description":
//                 return eventRepository.findEventDtoByDescriptionLike(value, Pageable.ofSize(10).withPage(pageNum));
//             case "location":
//                 return eventRepository.findEventDtoByLocationLike(value, Pageable.ofSize(10).withPage(pageNum));
//             default:
//                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                
//         }
//     }

//     public Page<EventDtoPublic> getEventsHappeningAtTime(Timestamp time, int pageNum){
//         return eventRepository.findEventDtoByDateBetween(time, Pageable.ofSize(10).withPage(pageNum));
//     }



//     private boolean canEditEvent(String userId,String eventId, EventDto eventDto){
//         if (userId != null && eventDto.userId() != null && userId == eventDto.userId() && eventDto.orgId() != null) {
//             MyUser user = manager.find(MyUser.class, userId);
//             MyOrg org = manager.find(MyOrg.class, eventDto.orgId());
//             if (user != null && org != null && user.getOrgs().contains(org)) {
//                 if (eventId != null) {
//                     MyEvent event = manager.find(MyEvent.class, eventId);
//                     if (event != null && event.getOrganiser()==org) {
//                         return true;
//                     }
//                 }else{
//                     return true;
//                 }
//             }
//         }
//         return false;
//     }

//     private boolean canDeleteEvent(String userId,String eventId){
//         if (userId != null && eventId != null) {
//             MyUser user = manager.find(MyUser.class, userId);
//             MyEvent event = manager.find(MyEvent.class, eventId);
//             if (event != null && user != null && user.getOrgs().contains(event.getOrganiser())) {
//                 return true;
//             }
//         }
//         return false;
//     }

//     private MyEvent mapEventDtoToMyEvent(MyEvent event, EventDto eventDto){
//         event.setDescription(eventDto.description());
//         event.setName(eventDto.name());
//         event.setLocation(eventDto.location());
//         MyOrg org = manager.find(MyOrg.class, eventDto.orgId());
//         event.setOrganiser(org);
//         MyUser user = manager.find(MyUser.class, eventDto.userId());
//         event.setOrganiserUser(user);
//         event.setStartDate(eventDto.startDate());
//         event.setEndDate(eventDto.endDate());
//         event.setPublicEmails(eventDto.publicEmails());
//         event.setPublicPhones(eventDto.publicPhones());
//         return event;
//     }
// }