package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.EventService;
import hu.project.groupproject.resourceserver.services.OrgService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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



@RestController
@RequestMapping("/org")
public class OrgController {

    protected final Log logger = LogFactory.getLog(getClass());

    OrgService orgService;
    EventService eventService;

    public OrgController(OrgService orgService, EventService eventService){
        this.orgService=orgService;
        this.eventService=eventService;
    }
    
    @PostMapping("/addAdmin")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void addUserToOrg(@RequestBody Map<String, String> body, Authentication authentication){
        MyUser user = (MyUser)authentication.getPrincipal();
        String adminId = body.get("adminId");
        if(user.getId()==adminId){
            String userId = body.get("userId");
            String orgId = body.get("orgId");
            orgService.addUser(orgId,adminId,userId);
        }
    }
    //should be more sophisticated so not any admin can remove any and all other admins...
    @PostMapping("/removeAdmin")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public void removeUserFromOrg(@RequestBody Map<String, String> body, Authentication authentication){
        MyUser user = (MyUser)authentication.getPrincipal();
        String adminId = body.get("adminId");
        if(user.getId()==adminId){
            String userId = body.get("userId");
            String orgId = body.get("orgId");
            orgService.removeUser(orgId,adminId,userId);
        }
    }
    
    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public ImageUploadDetailsDto createOrg(@RequestBody OrgDtoCreate org){
        return orgService.createOrg(org);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public ImageUploadDetailsDto updateOrg(@PathVariable String orgId, @RequestBody OrgDtoCreate org, Authentication authentication) throws InvalidAttributeValueException{
        MyUser user = (MyUser)authentication.getPrincipal();
        if (org.adminId()==user.getId()) {
            return orgService.saveOrg(orgId,org);
        }
        throw new AccessDeniedException("You don't have the right to change this organisation");
    }
    
    @GetMapping("/{id}")
    public Optional<OrgDtoPublic> getOrg(@PathVariable String id) {
        return orgService.getOrg(id);
    }
    
    @DeleteMapping("/del/{id}")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public void deleteOrg(@PathVariable String orgId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        orgService.deleteOrg(user.getId(),orgId);
    }

    @GetMapping("/{orgId}/events")
    public Set<EventDtoPublic> getPostsForUser(@PathVariable String orgId) {
        return eventService.getEventsForOrg(orgId);
    }
    
}
