package hu.project.groupproject.resourceserver.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.ImageUploadDetailsDto;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.orgs.OrgDtoPublicPartial;
import hu.project.groupproject.resourceserver.dtos.En.users.UserDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.enums.OrgCategory;
import hu.project.groupproject.resourceserver.repositories.OrgRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class OrgService {
    
    protected final Log logger = LogFactory.getLog(getClass());

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
        return new ImageUploadDetailsDto("images/"+newOrg.getPath(), false);
    }
    public ImageUploadDetailsDto saveOrg(String orgId,OrgDtoCreate org) throws InvalidAttributeValueException{
        MyOrg oldOrg = manager.find(MyOrg.class, orgId);
        MyUser admin = manager.find(MyUser.class, org.adminId());
        if (oldOrg == null || admin == null) {
            throw new InvalidAttributeValueException("reference to nonexistent entities");
        }
        oldOrg.setName(org.name());
        oldOrg.addUser(admin);
        //should save by itself
        return new ImageUploadDetailsDto("images/"+oldOrg.getPath(), false);
    }

    public void addUser(String orgId, String adminId, String userId){
        MyOrg org = manager.find(MyOrg.class, orgId);
        if (org != null) {
            MyUser admin = manager.find(MyUser.class, adminId);
            if (admin != null && org.getUsers().contains(admin)) {
                MyUser user = manager.find(MyUser.class, userId);
                if (user != null && !org.getUsers().contains(user)) {
                    org.addUser(user);
                }
            }
        }
    }
    public void removeUser(String orgId, String adminId, String userId){
        MyOrg org = manager.find(MyOrg.class, orgId);
        if (org != null) {
            MyUser admin = manager.find(MyUser.class, adminId);
            if (admin != null && org.getUsers().contains(admin)) {
                MyUser user = manager.find(MyUser.class, userId);
                if (user != null && org.getUsers().contains(user)) {
                    Set<MyUser> users = org.getUsers();
                    if(users.remove(user)){
                        org.setUsers(users);
                    }
                }
            }
        }
    }

    public Set<String> getEventsIdsForOrg(String id){
        Set<String> eventIds = orgRepository.findEventIdsByOrgId(id);
        logger.debug(eventIds);
        return eventIds;
    }
    public Set<String> getReservationsIdsForOrg(String id){
        Set<String> reservationId = orgRepository.findReservationIdsByOrgId(id);
        logger.debug(reservationId);
        return reservationId;
    }

    public OrgDtoPublic addImages(OrgDtoPublicPartial org){
        MyOrg temp = manager.find(MyOrg.class, org.id());
        return new OrgDtoPublic(org.id(), org.name(), temp.getUrls());
    }

    public Optional<OrgDtoPublic> getOrg(String id){
        return orgRepository.findById(id, OrgDtoPublic.class);
    }
    public Set<OrgDtoPublic> getOrgs(int pageNum){
        Page<MyOrg> orgs = orgRepository.findAll(Pageable.ofSize(10).withPage(pageNum));
        Set<OrgDtoPublic> orgDtos = new HashSet<>();
        orgs.forEach((e)->{
            orgDtos.add(mapOrgToDto(e));
        });
        return orgDtos;
    }

    public Set<OrgDtoPublicPartial> getOrgsByUserId(String userId, int pageNum){
        Page<MyOrg> orgs = orgRepository.findOrgByUserId(userId, Pageable.ofSize(10).withPage(pageNum));
        Set<OrgDtoPublicPartial> orgDtos = new HashSet<>();
        orgs.forEach((e)->{
            orgDtos.add(mapOrgToDtoPartial(e));
        });
        return orgDtos;
    }

    public Set<OrgCategory> addOrRemoveCategory(String userId,String orgId,OrgCategory category){
        if (canDeleteOrEditOrg(userId, orgId)) {
            MyOrg org = manager.find(MyOrg.class, orgId);
            if (org.getCategories() != null && org.getCategories().contains(category)) {
                org.removeCategory(category);
            }else{
                org.addCategory(category);
            }
            return org.getCategories();
        }
        throw new AccessDeniedException("You don't have the right to change this organisation");
    } 

    public Set<OrgDtoPublic> getOrgsByCategory(int pageNum, OrgCategory category){
        Page<MyOrg> orgs = orgRepository.findOrgsByCategory(category, Pageable.ofSize(10).withPage(pageNum));
        Set<OrgDtoPublic> orgDtos = new HashSet<>();
        orgs.forEach(this::mapOrgToDto);
        return orgDtos;
    }
    public Set<OrgDtoPublic> getOrgsByNameLike(int pageNum, String name){
        Page<MyOrg> orgs = orgRepository.findOrgByNameLike(name, Pageable.ofSize(10).withPage(pageNum));
        Set<OrgDtoPublic> orgDtos = new HashSet<>();
        orgs.forEach(this::mapOrgToDto);
        return orgDtos;
    }

    private OrgDtoPublic mapOrgToDto(MyOrg org){
        return new OrgDtoPublic(org.getId(), org.getName(), org.getUrls());
    }
    private OrgDtoPublicPartial mapOrgToDtoPartial(MyOrg org){
        return new OrgDtoPublicPartial(org.getId(), org.getName());
    }

    @SuppressWarnings("null")
    public void deleteOrg(String userId, String orgId){
        if (canDeleteOrEditOrg(userId, orgId)) {
            orgRepository.deleteById(orgId);
        }
    }

    private boolean canDeleteOrEditOrg(String userId, String orgId){
        if (orgId != null && userId != null) {
            MyUser user =manager.find(MyUser.class, userId);
            if (user != null) {
                MyOrg myOrg = manager.find(MyOrg.class, orgId);
                if (myOrg != null && user.getOrgs().contains(myOrg)) {
                    return true;
                }
            }
        }
        return false;
    }
}
