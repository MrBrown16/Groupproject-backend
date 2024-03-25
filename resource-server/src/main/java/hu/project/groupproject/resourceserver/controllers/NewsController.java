package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoCreate;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.NewsService;

import java.sql.Timestamp;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;


//not in use, news will be a separate thing i guess
@RestController
@RequestMapping("/news")
public class NewsController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    NewsService newsService;
    
    public NewsController(NewsService newsService){
        this.newsService=newsService;
    }
    
    // use /{id} getNewsEx instead
    // @GetMapping("/{id}/short")
    // public Optional<NewsDtoPublicWithImages> getNewsShort(@PathVariable String id) {
    //     return newsService.getNewsShort(id);
    // }

    @GetMapping("/{id}")
    public Optional<NewsDtoPublic> getNewsEx(@PathVariable String id) {
        return newsService.getNews(id);
    }
    @GetMapping("/search/content")
    public Page<NewsDtoPublic> getNewssByContentLike(@RequestParam("value") String value, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
        return newsService.getNewsByPropertyLike(value,pageNum,category);
    }
    @GetMapping("/search/time")
    public Page<NewsDtoPublic> getNewssByTime(@RequestParam("time") Timestamp time, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
        return newsService.getNewsByTimeLike(time,pageNum,category);
    }
    
    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public NewsDtoPublic saveNews(@RequestBody NewsDtoCreate news){
        return newsService.createNews(news);
    }

    @PutMapping("/{id}") 
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public NewsDtoPublic updateNews(@PathVariable("id") String id, @RequestBody NewsDtoCreate news, Authentication auth) throws NotFoundException{
        MyUser user = (MyUser)auth.getPrincipal();
        if (user.getId()==news.userId() && id != null) {
            return newsService.updateNews(id,news);
        }
        throw new AccessDeniedException("You don't have the right to change this news");
    }

    
    @DeleteMapping("/del/{newsId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public void deleteNews(@PathVariable String newsId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        logger.debug(user.getId());
        newsService.deleteNews(user.getId(), newsId);
    }
    
}
