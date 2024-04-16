package hu.project.groupproject.resourceserver.controllers;

import org.springframework.web.bind.annotation.RestController;

import hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic;
import hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoCreate;
import hu.project.groupproject.resourceserver.services.NewsService;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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


@RestController
@RequestMapping("/news")
public class NewsController {

    protected final Log logger = LogFactory.getLog(getClass());
    
    NewsService newsService;
    
    public NewsController(NewsService newsService){
        this.newsService=newsService;
    }

    @GetMapping("/{id}")
    public Optional<NewsDtoPublic> getNews(@PathVariable String id) {
        return newsService.getNews(id);
    }
    @GetMapping("/sajat/{id}")
    public Set<NewsDtoPublic> getNewsForUser(@PathVariable String id) {
        return newsService.getNewsForUser(id);
    }

    //TODO: hasznalt
    @GetMapping("/search")
    public Page<NewsDtoPublic> getNewssByPropertyLike(@RequestParam("value") String value, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
        return newsService.getNewsByPropertyLike(value,pageNum,category);
    }
    @GetMapping("/search/time")
    public Page<NewsDtoPublic> getNewssByTime(@RequestParam("time") Timestamp time, @RequestParam("pageNum") int pageNum, @RequestParam("category") String category ) {
        return newsService.getNewsByTimeLike(time,pageNum,category);
    }
    
    //TODO: hasznalt
    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public NewsDtoPublic saveNews(@RequestBody NewsDtoCreate news, Authentication auth){
        return newsService.createNews(news,auth);
    }

    //TODO: hasznalt
    @PutMapping("/{newsId}") 
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public NewsDtoPublic updateNews(@PathVariable("newsId") String newsId, @RequestBody NewsDtoCreate news, Authentication auth) throws NotFoundException{
        return newsService.updateNews(newsId,news,auth);
    }

    //TODO: hasznalt
    @DeleteMapping("/del/{newsId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN','USER')")
    public void deleteNews(@PathVariable String newsId, Authentication auth) {
        newsService.deleteNews(newsId, auth);
    }
    
}
