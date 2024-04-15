package hu.project.groupproject.resourceserver.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.En.NoticeDto;
import hu.project.groupproject.resourceserver.dtos.En.NoticeDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNotice;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.enums.NoticeTypes;
import hu.project.groupproject.resourceserver.repositories.NoticeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class NoticeService {
    
    private final Log logger = LogFactory.getLog(getClass());


    @PersistenceContext
    EntityManager manager;


    NoticeRepository noticeRepository;
    UserService userService;


    public NoticeService(NoticeRepository noticeRepository, UserService userService) {
        this.noticeRepository = noticeRepository;
        this.userService=userService;
    }

    @Transactional
    public void createNotice(NoticeDto noticeDto, Authentication auth){
        MyUser user = (MyUser) auth.getPrincipal();
        user = manager.find(MyUser.class, user.getId());
        if (user != null && noticeDto.userId() != null && user.getId().equals(noticeDto.userId())) {    
            logger.debug("allowed for self createNotice");
            MyNotice notice = new MyNotice();
            notice = mapNoticeDtoToMyNotice(notice, noticeDto);
            manager.persist(notice);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You don't have the right to create this notice");
        }
    }
    @Transactional
    public void updateNotice(NoticeDtoPublic noticeDto, Authentication auth){
        MyUser user = (MyUser) auth.getPrincipal();
        user = manager.find(MyUser.class, user.getId());
        if (user != null && noticeDto.userId() != null && user.getId().equals(noticeDto.userId())) { 
            logger.debug("allowed for self updateNotice");
            MyNotice notice = manager.find(MyNotice.class, noticeDto.noticeId());
            notice = mapNoticeDtoToMyNotice(notice, noticeDto);
            manager.persist(notice);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"You don't have the right to modify this notice");
        }
    }

    public void deleteNotice(String noticeId, Authentication auth){
        MyNotice notice = manager.find(MyNotice.class, noticeId);
        if (canDeleteNotice(notice, auth)) {
            noticeRepository.delete(notice);
        }
    }

    public Optional<NoticeDtoPublic> getNotice(String noticeId){
        return noticeRepository.findNoticeDtoById(noticeId);        
    }

    public Set<NoticeDtoPublic> getNoticesForUser(String userId){
        Set<String> noticeIds = userService.getNoticesIdsForUser(userId);
        Set<NoticeDtoPublic> notices = new HashSet<>();
        for (String noticeId : noticeIds) {
            Optional<NoticeDtoPublic> notice = getNotice(noticeId);
            if (notice.isPresent()) {
                notices.add(notice.get());
            }
        }
        return notices;
    }

    private boolean canDeleteNotice(MyNotice notice, Authentication auth){
        MyUser user = (MyUser) auth.getPrincipal();
        user = manager.find(MyUser.class, user.getId());
        if (notice != null && user != null && notice.getUser().equals(user)) {
            return true;
        }
        return false;
    }

    private MyNotice mapNoticeDtoToMyNotice(MyNotice notice, NoticeDto noticeDto){
        notice.setType(NoticeTypes.valueOf( noticeDto.type()));
        notice.setUrgency(noticeDto.urgency());
        notice.setDescription(noticeDto.description());
        notice.setLocation(noticeDto.location());
        notice.setPhone(noticeDto.phone());
        MyUser user = manager.find(MyUser.class, noticeDto.userId());
        notice.setUser(user);
        notice.setDate(noticeDto.date());
        return notice;
    }
    private MyNotice mapNoticeDtoToMyNotice(MyNotice notice, NoticeDtoPublic noticeDto){
        notice.setId(noticeDto.noticeId());
        notice.setType(NoticeTypes.valueOf( noticeDto.type()));
        notice.setUrgency(noticeDto.urgency());
        notice.setDescription(noticeDto.description());
        notice.setLocation(noticeDto.location());
        notice.setPhone(noticeDto.phone());
        MyUser user = manager.find(MyUser.class, noticeDto.userId());
        notice.setUser(user);
        notice.setDate(noticeDto.date());
        return notice;
    }
    
}
