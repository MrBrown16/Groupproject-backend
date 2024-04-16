package hu.project.groupproject.resourceserver.controllers;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.NoticeDto;
import hu.project.groupproject.resourceserver.dtos.En.NoticeDtoPublic;
import hu.project.groupproject.resourceserver.enums.NoticeTypes;
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

    // @GetMapping("/")
    // public Optional<NoticeDtoPublic> getNoticesForAdmin(@PathVariable String noticeId) {
    //     // return noticeService.getNotices();
    // }

    //TODO: hasznalt
    @GetMapping("/search")
    public Page<NoticeDtoPublic> getNewssByPropertyLike(@RequestParam("pageNum") int pageNum, @RequestParam("category") NoticeTypes category ) {
        return noticeService.getNewsByPropertyLike(pageNum,category);
    }
    @GetMapping("/{noticeId}")
    public Optional<NoticeDtoPublic> getNotice(@PathVariable String noticeId) {
        return noticeService.getNotice(noticeId);
    }

    //TODO: hasznalt
    @GetMapping("/sajat/{userId}")
    public Set<NoticeDtoPublic> getNoticesForUser(@PathVariable String userId) {
        return noticeService.getNoticesForUser(userId);
    }

    //TODO: hasznalt
    @GetMapping("/org/{orgId}")
    public Set<NoticeDtoPublic> getNoticesForOrg(@PathVariable String orgId,@RequestParam("pageNum") int pageNum) {
        return noticeService.getNoticesForOrg(orgId,pageNum);
    }

    //TODO: hasznalt
    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public void saveNotice(@RequestBody NoticeDto notice, Authentication auth){
        noticeService.createNotice(notice,auth);
    }

    //TODO: hasznalt
    @PutMapping("/{noticeId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public void updateNotice(@PathVariable String noticeId, @RequestBody NoticeDtoPublic notice, Authentication auth){
        noticeService.updateNotice(notice, auth);
    }

    //TODO: hasznalt
    @DeleteMapping("/del/{noticeId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteNotice(@PathVariable String noticeId, Authentication auth) {
        noticeService.deleteNotice(noticeId,auth);
    }

}
