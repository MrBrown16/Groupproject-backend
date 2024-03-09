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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.EventDto;
import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.EventService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@RequestMapping("/event")
public class EventController {
    
    protected final Log logger = LogFactory.getLog(getClass());

    @PersistenceContext
    EntityManager manager;

    EventService eventService;

    public EventController(EventService eventService){
        this.eventService=eventService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void saveEvent(@RequestBody EventDto event, Authentication authentication){
        MyUser user = (MyUser)authentication.getPrincipal();
        eventService.createEvent(user.getId(),event);
    }

    @PutMapping("/{eventId}") 
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void updateEvent(@PathVariable String eventId,@RequestBody EventDto event, Authentication auth) throws NotFoundException{
        MyUser user = (MyUser)auth.getPrincipal();
        if (user.getId()==event.userId()) {
            eventService.updateEvent(user.getId(),eventId,event);
        }
        throw new AccessDeniedException("You don't have the right to change this event");
    }
    @GetMapping("/{eventId}")
    public Optional<EventDtoPublic> getEvent(@PathVariable String eventId) {
        return eventService.getEvent(eventId);
    }
    
    @DeleteMapping("/del/{eventId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void deleteEvent(@PathVariable String eventId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        eventService.deleteEvent(user.getId(), eventId);
    }


}
