package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.ReservationDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublicPartial;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.enums.NoticeTypes;
import hu.project.groupproject.resourceserver.enums.OrgCategory;
import hu.project.groupproject.resourceserver.services.EventService;
import hu.project.groupproject.resourceserver.services.OrgService;
import hu.project.groupproject.resourceserver.services.ReservationService;
import hu.project.groupproject.resourceserver.services.UserService;

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
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/org")
public class OrgController {

    protected final Log logger = LogFactory.getLog(getClass());

    OrgService orgService;
    EventService eventService;
    ReservationService reservationService;
    UserService userService;
    
    public OrgController(OrgService orgService, EventService eventService, ReservationService reservationService,UserService userService){
        this.orgService=orgService;
        this.eventService=eventService;
        this.reservationService =reservationService;
        this.userService=userService;
    }
    
    @GetMapping("/")
    public Set<OrgDtoPublic> getOrgs(int pageNum) {
        return orgService.getOrgs(pageNum);
    }
    @GetMapping("/{id}")
    public Optional<OrgDtoPublic> getOrg(@PathVariable String id) {
        return orgService.getOrg(id);
    }
    @GetMapping("/users/{orgId}")
    public Set<UserDtoPublic> getOrgUsers(@PathVariable String orgId) {
        return userService.getUsersForOrg(orgId);
    }
    @GetMapping("/search/category")
    public Set<OrgDtoPublic> getOrgsByCategory(@RequestParam("pageNum") int pageNum, @RequestParam("category") OrgCategory category) {
        return orgService.getOrgsByCategory(pageNum, category);
    }
    @GetMapping("/search/category/part")
    public Set<OrgDtoPublicPartial> getOrgsByCategoryPart(@RequestParam("pageNum") int pageNum, @RequestParam("category") OrgCategory category) {
        return orgService.getOrgsByCategoryPart(pageNum, category);
    }
    @GetMapping("/search/name/full")
    public Set<OrgDtoPublic> getOrgsByNameLikeFull(@RequestParam("pageNum") int pageNum, @RequestParam("name") String name) {
        return orgService.getOrgsByNameLike(pageNum, name);
    }
    //TODO: hasznalt
    @GetMapping("/search/name")
    public Set<OrgDtoPublicPartial> getOrgsByNameLike(@RequestParam("pageNum") int pageNum, @RequestParam("name") String name) {
        return orgService.getOrgsByNameLikePart(pageNum, name);
    }

    @GetMapping("/{orgId}/reservations")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public Set<ReservationDtoPublic> getReservationsForOrg(@PathVariable String orgId) {
        return reservationService.getReservationsForOrg(orgId);
    }
    
    @PostMapping("/addAdmin")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void addUserToOrg(@RequestBody Map<String, String> body, Authentication auth){
        MyUser user = (MyUser)auth.getPrincipal();
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
    public void removeUserFromOrg(@RequestBody Map<String, String> body, Authentication auth){
        MyUser user = (MyUser)auth.getPrincipal();
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
    public ImageUploadDetailsDto updateOrg(@PathVariable String orgId, @RequestBody OrgDtoCreate org, Authentication auth) throws InvalidAttributeValueException{
        MyUser user = (MyUser)auth.getPrincipal();
        if (org.adminId()==user.getId()) {
            return orgService.saveOrg(orgId,org);
        }
        throw new AccessDeniedException("You don't have the right to change this organisation");
    }
    @PutMapping("/{orgId}/{category}")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public Set<OrgCategory> addOrRemoveCategory(@PathVariable("orgId") String orgId, @PathVariable("category") OrgCategory category, Authentication auth) throws InvalidAttributeValueException{
        MyUser user = (MyUser)auth.getPrincipal();
        return orgService.addOrRemoveCategory(user.getId(), orgId,category);
    }
    @PutMapping("/setResponsibility")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean setResponsibilityForOrg(@RequestParam("orgId") String orgId, @PathVariable("types") Set<NoticeTypes> types, Authentication auth) throws InvalidAttributeValueException{
        return orgService.addResponsibility(auth, orgId,types);
    }
    @PutMapping("/removeResponsibility")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean removeResponsibilityForOrg(@RequestParam("orgId") String orgId, @PathVariable("types") NoticeTypes types, Authentication auth) throws InvalidAttributeValueException{
        return orgService.removeResponsibility(auth, orgId,types);
    }
    
    
    @DeleteMapping("/del/{id}")
    @PreAuthorize("hasRole('ORG_ADMIN')")
    public void deleteOrg(@PathVariable String orgId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        orgService.deleteOrg(user.getId(),orgId);
    }

    
}
