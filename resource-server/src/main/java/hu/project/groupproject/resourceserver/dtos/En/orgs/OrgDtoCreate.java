package hu.project.groupproject.resourceserver.dtos.En.orgs;
//if the orgs need to be checked if they exist on creation in the real world 
//the additional information would need to be included here
public record OrgDtoCreate(String adminId, String name){}
