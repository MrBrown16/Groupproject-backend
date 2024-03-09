package hu.project.groupproject.resourceserver.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.En.NoticeDto;
import hu.project.groupproject.resourceserver.dtos.En.NoticeDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNotice;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.NoticeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class NoticeService {
    


    @PersistenceContext
    EntityManager manager;


    NoticeRepository noticeRepository;
    UserService userService;


    public NoticeService(NoticeRepository noticeRepository, UserService userService) {
        this.noticeRepository = noticeRepository;
        this.userService=userService;
    }

    public void createNotice(String userId, NoticeDto noticeDto){
        if (userId != null && noticeDto.userId() != null && userId == noticeDto.userId()) {    
            MyNotice notice = new MyNotice();
            notice = mapNoticeDtoToMyNotice(notice, noticeDto);
            manager.persist(notice);
        }
    }

    public void deleteNotice(String userId,String noticeId){
        if (canDeleteNotice(userId, noticeId)) {
            MyNotice notice = manager.find(MyNotice.class, noticeId);
            if (notice != null) {
                noticeRepository.delete(notice);
            }
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

    private boolean canDeleteNotice(String userId,String noticeId){
        if (userId != null && noticeId != null) {
            MyUser user = manager.find(MyUser.class, userId);
            MyNotice notice = manager.find(MyNotice.class, noticeId);
            if (notice != null && user != null && notice.getUser()==user) {
                return true;
            }
        }
        return false;
    }

    private MyNotice mapNoticeDtoToMyNotice(MyNotice notice, NoticeDto noticeDto){
        notice.setType(noticeDto.type());
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
