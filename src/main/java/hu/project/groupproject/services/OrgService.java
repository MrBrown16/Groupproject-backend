package hu.project.groupproject.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.project.groupproject.dtos.orgDTOs.OrgDTOPublic;
import hu.project.groupproject.entities.MyOrg;
import hu.project.groupproject.repositories.OrgRepository;


@Service
public class OrgService {
    
    OrgRepository orgRepository;

    // @PersistenceContext
    // EntityManager entityManager;
    
    public OrgService(OrgRepository orgRepository){
        this.orgRepository=orgRepository;
    }
   
    public MyOrg saveOrg(MyOrg myOrg){
        return orgRepository.save(myOrg);
    }
    public Optional<OrgDTOPublic> getOrg(Long id){
        return orgRepository.findById(id, OrgDTOPublic.class);
    }

    public void deleteOrg(MyOrg user){
        orgRepository.delete(user);
    }

}
