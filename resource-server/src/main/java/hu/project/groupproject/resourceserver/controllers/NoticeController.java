package hu.project.groupproject.resourceserver.controllers;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/new")
    @PreAuthorize("hasRole('USER')")
    public void saveNotice(@RequestBody NoticeDto notice, Authentication authentication){
        MyUser user = (MyUser)authentication.getPrincipal();
        noticeService.createNotice(user.getId(),notice);
    }

    @DeleteMapping("/del/{noticeId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteNotice(@PathVariable String noticeId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        noticeService.deleteNotice(user.getId(), noticeId);
    }

}
