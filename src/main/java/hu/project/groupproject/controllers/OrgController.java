package hu.project.groupproject.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.dtos.orgDTOs.OrgDTOPublic;
import hu.project.groupproject.entities.MyOrg;
import hu.project.groupproject.services.OrgService;

import java.util.Optional;

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

    @PostMapping
    public MyOrg saveOrg(@RequestBody MyOrg org){
        return orgService.saveOrg(org);
    }

    @GetMapping("/{id}")
    public Optional<OrgDTOPublic> getOrg(@PathVariable Long id) {
        return orgService.getOrg(id);
    }
    
    @PostMapping("/del")
    public void deleteOrg(@RequestBody MyOrg org) {
        orgService.deleteOrg(org);
    }
    
}
