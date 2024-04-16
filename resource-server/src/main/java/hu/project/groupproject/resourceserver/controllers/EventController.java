package hu.project.groupproject.resourceserver.controllers;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.EventDto;
import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
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
    
    @GetMapping("/{eventId}")
    public Optional<EventDtoPublic> getEvent(@PathVariable String eventId) {
        return eventService.getEvent(eventId);
    }

    //TODO: hasznalt
    @GetMapping("/search")
    public Page<EventDtoPublic> getPostsByContentLike(@RequestParam("value") String value, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category) {
        return eventService.getEventsByPropertyLike(pageNum,value,category);
    }

    @GetMapping("/search/time")
    public Page<EventDtoPublic> getPostsByTime(@RequestParam("time") Timestamp time, @RequestParam("pageNum") int pageNum) {
        return eventService.getEventsHappeningAtTime(time,pageNum);
    }

    //TODO: hasznalt
    @GetMapping("/sajat/{orgId}")
    public Set<EventDtoPublic> getEventsForOrg(@PathVariable String orgId) {
        return eventService.getEventsForOrg(orgId);
    }

    //TODO: hasznalt
    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void saveEvent(@RequestBody EventDto event, Authentication auth){
        eventService.createEvent(event,auth);
    }

    //TODO: hasznalt
    @PutMapping("/{eventId}") 
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void updateEvent(@PathVariable String eventId,@RequestBody EventDto event, Authentication auth) throws NotFoundException{
        eventService.updateEvent(eventId,event,auth);
    }
    
    //TODO: hasznalt
    @DeleteMapping("/del/{eventId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void deleteEvent(@PathVariable String eventId, Authentication auth) {

        eventService.deleteEvent(eventId, auth);
    }


}
