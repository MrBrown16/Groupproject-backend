package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.services.OrgService;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/org")
public class OrgController {

    OrgService orgService;

    public OrgController(OrgService orgService){
        this.orgService=orgService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public ImageUploadDetailsDto createOrg(@RequestBody OrgDtoCreate org){
        return orgService.createOrg(org);
    }

    @GetMapping("/{id}")
    public Optional<OrgDtoPublic> getOrg(@PathVariable String id) {
        return orgService.getOrg(id);
    }
    
    @PostMapping("/del/{id}")
    public void deleteOrg(@PathVariable String orgId) {
        orgService.deleteOrg(orgId);
    }
    
}
