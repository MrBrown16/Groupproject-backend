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
    @Transactional
    public void createEvent(EventDto eventDto,Authentication auth){
        if (canEditEvent(null, eventDto,auth)) {
            MyEvent event = new MyEvent();
            event = mapEventDtoToMyEvent(event, eventDto);
            manager.persist(event);
            
        }
    }
    @Transactional
    public void updateEvent(String eventId, EventDto eventDto, Authentication auth){
        MyEvent event = manager.find(MyEvent.class, eventId);
        if (canEditEvent(event, eventDto,auth)) {
            event = mapEventDtoToMyEvent(event, eventDto);
            manager.persist(event);
        }
    }
    @Transactional
    public void deleteEvent(String eventId, Authentication auth){
        MyUser user = (MyUser)auth.getPrincipal();
        user = manager.find(MyUser.class, user.getId());
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



    // private boolean canEditEvent(String eventId, EventDto eventDto,Authentication auth){
    //     logger.debug("canEditEvent authorities: "+auth.getAuthorities());
    //     if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
    //         return true;
    //     }
    //     MyUser userr = (MyUser)auth.getPrincipal();
    //     String userId = userr.getId();
    //     if (userId != null && eventDto.userId() != null && userId.equals(eventDto.userId()) && eventDto.orgId() != null) {
    //         MyUser user = manager.find(MyUser.class, userId);
    //         MyOrg org = manager.find(MyOrg.class, eventDto.orgId());
    //         if (user != null && org != null && user.getOrgs().contains(org)) {
    //             if (eventId != null) {
    //                 MyEvent event = manager.find(MyEvent.class, eventId);
    //                 if (event != null && event.getOrganiser().equals(org)) {
    //                     return true;
    //                 }
    //             }else{
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }
    private boolean canEditEvent(MyEvent event, EventDto eventDto,Authentication auth){
        if (eventDto == null) {
            return false;
        }
        if (event != null) {
            
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                logger.debug("admin so allowed canEditEvent");
                return true;
            }
            
            MyUser user = (MyUser)auth.getPrincipal();
            String userId = user.getId();
            if (userId != null && eventDto.userId() != null && userId.equals(eventDto.userId()) && eventDto.orgId() != null) {
                user = manager.find(MyUser.class, userId);
                if (user != null && event.getOrganiser() != null  && user.getOrgs().contains(event.getOrganiser())) {
                    return true;
                }
            }
        }else{
            MyUser user = (MyUser)auth.getPrincipal();
            String userId = user.getId();
            if (userId != null && eventDto.userId() != null && userId.equals(eventDto.userId()) && eventDto.orgId() != null) {
                user = manager.find(MyUser.class, userId);
                MyOrg org = manager.find(MyOrg.class, eventDto.orgId());
                if (user != null && org != null && user.getOrgs().contains(org)) {
                    return true;
                }
            }
        }
        return false;
    }

    // private boolean canDeleteEvent(String userId,String eventId, Authentication auth){
    //     if (userId != null && eventId != null) {
    //         MyUser user = manager.find(MyUser.class, userId);
    //         MyEvent event = manager.find(MyEvent.class, eventId);
    //         if (event != null && user != null && user.getOrgs().contains(event.getOrganiser())) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    private boolean canDeleteEvent(MyUser user,MyEvent event, Authentication auth){
        if (event != null) {
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                logger.debug("admin so allowed canDeleteEvent");
                return true;
            }
            if (user != null && user.getOrgs().contains(event.getOrganiser())) {
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
