package hu.project.groupproject.resourceserver.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.OrgRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class OrgService {
    
    OrgRepository orgRepository;

    @PersistenceContext
    EntityManager manager;

    
    public OrgService(OrgRepository orgRepository){
        this.orgRepository=orgRepository;
    }
   
    public ImageUploadDetailsDto createOrg(OrgDtoCreate org){
        MyOrg newOrg = new MyOrg();
        MyUser admin = manager.find(MyUser.class, org.adminId());
        if (admin!=null && org.name()!=null && org.name().length()>4) {
            newOrg.addUser(admin);
            newOrg.setName(org.name());
        }
        return new ImageUploadDetailsDto("images/"+newOrg.getPath(), false) ;
    }
    public MyOrg saveOrg(MyOrg myOrg){
        return orgRepository.save(myOrg);
    }
    public Optional<OrgDtoPublic> getOrg(String id){
        return orgRepository.findById(id, OrgDtoPublic.class);
    }

    public void deleteOrg(String orgId){
        orgRepository.deleteById(orgId);
    }

}
