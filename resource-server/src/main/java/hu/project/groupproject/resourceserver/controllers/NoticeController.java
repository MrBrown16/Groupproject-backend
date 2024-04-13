package hu.project.groupproject.resourceserver.controllers;

import java.util.Optional;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.NoticeDto;
import hu.project.groupproject.resourceserver.dtos.En.NoticeDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.NoticeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    
protected final Log logger = LogFactory.getLog(getClass());

    @PersistenceContext
    EntityManager manager;

    NoticeService noticeService;

    public NoticeController(NoticeService noticeService){
        this.noticeService=noticeService;
    }

    @GetMapping("/{noticeId}")
    public Optional<NoticeDtoPublic> getNotice(@PathVariable String noticeId) {
        return noticeService.getNotice(noticeId);
    }
    @GetMapping("/sajat/{userId}")
    public Set<NoticeDtoPublic> getNoticesForUser(@PathVariable String userId) {
        return noticeService.getNoticesForUser(userId);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public void saveNotice(@RequestBody NoticeDto notice, Authentication auth){
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        noticeService.createNotice(user.getId(),notice);
    }
    @PutMapping("/{noticeId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public void updateNotice(@PathVariable String noticeId, @RequestBody NoticeDtoPublic notice, Authentication auth){
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        if (noticeId != null && notice.noticeId() != null && notice.noticeId().equals(noticeId)) {
            noticeService.updateNotice(user.getId(),notice);
        }else{
            throw new AccessDeniedException("You don't have the right to change this notice");
        }

    }

    @DeleteMapping("/del/{noticeId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteNotice(@PathVariable String noticeId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        noticeService.deleteNotice(user.getId(), noticeId);
    }

}
