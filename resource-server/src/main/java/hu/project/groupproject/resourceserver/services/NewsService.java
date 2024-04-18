package hu.project.groupproject.resourceserver.services;

import java.util.Optional;
import java.util.Set;
import java.sql.Timestamp;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoCreate;
import hu.project.groupproject.resourceserver.dtos.En.news.NewsDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNews;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.enums.NewsTypes;
import hu.project.groupproject.resourceserver.repositories.NewsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class NewsService {

    protected final Log logger = LogFactory.getLog(getClass());

    @PersistenceContext
    EntityManager manager;

    NewsRepository newsRepository;
    UserService userService;
    OrgService orgService;

    public NewsService(NewsRepository newsRepository, UserService userService, OrgService orgService) {
        this.newsRepository = newsRepository;
        this.userService = userService;
        this.orgService=orgService;
    }

    @SuppressWarnings("null")
    public NewsDtoPublic updateNews(String newsId, NewsDtoCreate newsUpdate, Authentication auth)
            throws NotFoundException {
        MyNews news = manager.find(MyNews.class, newsId);
        if (canEditNews(news, newsUpdate,auth)) {
            news.setTitle(newsUpdate.title());
            news.setContent(newsUpdate.content());
            news.setType(NewsTypes.valueOf(newsUpdate.type()));
            news = newsRepository.save(news);
            return new NewsDtoPublic(news.getId(), news.getUser().getId(), news.getUser().getUserName(),
                    news.getOrg().getId(), news.getOrg().getName(), news.getTitle(), news.getContent(),
                    news.getType());
        }
        throw new AccessDeniedException("You don't have the right to change this news");
    }

    public NewsDtoPublic createNews(NewsDtoCreate newsCreate, Authentication auth) {
        MyNews news = new MyNews();
        if (canEditNews(null, newsCreate, auth)) {

            news.setUser(manager.find(MyUser.class, newsCreate.userId()));
            news.setOrg(manager.find(MyOrg.class, newsCreate.orgId()));
            news.setTitle(newsCreate.title());
            news.setContent(newsCreate.content());
            news.setType(NewsTypes.valueOf(newsCreate.type()));
            news = newsRepository.save(news);
            return new NewsDtoPublic(news.getId(), news.getUser().getId(), news.getUser().getUserName(),
                    news.getOrg().getId(), news.getOrg().getName(), news.getTitle(), news.getContent(), news.getType());
        }
        throw new AccessDeniedException("You don't have the right to change this news");
    }

    public Optional<NewsDtoPublic> getNews(String id) {

        return newsRepository.findNewsDtoById(id);
    }

    public Set<NewsDtoPublic> getNewsForUser(String userId) {
        Set<String> newsIds = userService.getNewsIdsForUser(userId);
        Set<NewsDtoPublic> news = new HashSet<>();
        for (String newsId : newsIds) {
            Optional<NewsDtoPublic> newsOpt = getNews(newsId);
            if (newsOpt.isPresent()) {
                news.add(newsOpt.get());
            }
        }
        return news;

    }
    public Set<NewsDtoPublic> getNewsForOrg(String orgId) {
        Set<String> newsIds = orgService.getNewsIdsForOrg(orgId);
        Set<NewsDtoPublic> news = new HashSet<>();
        for (String newsId : newsIds) {
            Optional<NewsDtoPublic> newsOpt = getNews(newsId);
            if (newsOpt.isPresent()) {
                news.add(newsOpt.get());
            }
        }
        return news;

    }

    public Page<NewsDtoPublic> getNewsByPropertyLike(String search, int pageNum, String category) {
        Page<NewsDtoPublic> news;
        switch (category) {
            case "title":
                news = newsRepository.findNewsDtoByTitleLike(search, Pageable.ofSize(10).withPage(pageNum));

                break;
            case "content":
                news = newsRepository.findNewsDtoByContentLike(search, Pageable.ofSize(10).withPage(pageNum));

                break;
            case "type":
                news = newsRepository.findNewsDtoByTypeLike(search, Pageable.ofSize(10).withPage(pageNum));

                break;

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return news;
    }

    public Page<NewsDtoPublic> getNewsByTimeLike(Timestamp time, int pageNum, String category) {
        Page<NewsDtoPublic> news;
        switch (category) {
            case "updateBefore":
                news = newsRepository.findNewsDtoByUpdateTimeBefore(time, Pageable.ofSize(10).withPage(pageNum));

                break;
            case "updateAfter":
                news = newsRepository.findNewsDtoByUpdateTimeAfter(time, Pageable.ofSize(10).withPage(pageNum));

                break;
            case "createBefore":
                news = newsRepository.findNewsDtoByCreationTimeBefore(time, Pageable.ofSize(10).withPage(pageNum));

                break;
            case "createAfter":
                news = newsRepository.findNewsDtoByCreationTimeAfter(time, Pageable.ofSize(10).withPage(pageNum));

                break;

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return news;
    }

    public void deleteNews(String newsId, Authentication auth) {
        MyNews news = manager.find(MyNews.class, newsId);
        if (canDeleteNews(auth, news)) {
            newsRepository.deleteById(newsId);
        }
    }

    // private boolean canEditNews(String newsId, NewsDtoCreate news) {
    //     if (news.userId() != null && news.orgId() != null) {
    //         MyUser user = manager.find(MyUser.class, news.userId());
    //         if (user != null) {
    //             MyOrg org = manager.find(MyOrg.class, news.orgId());
    //             if (org != null && user.getOrgs().contains(org) && org.getUsers().contains(user)) {
    //                 if (newsId != null) {
    //                     MyNews myNews = manager.find(MyNews.class, newsId);
    //                     if (myNews.getOrg().getId().equals(news.orgId())) {
    //                         return true;
    //                     }
    //                     return false;
    //                 } else {
    //                     return true;
    //                 }
    //             }
    //         }
    //     }
    //     return false;
    // }

    private boolean canEditNews(MyNews news, NewsDtoCreate newsCreate, Authentication auth) {
        if (newsCreate == null) {
            return false;
        }

        if (news != null) {

            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                logger.debug("admin so allowed canEditNews");
                return true;
            }

            MyUser user = (MyUser) auth.getPrincipal();
            user = manager.find(MyUser.class, user.getId());
            if (user != null && newsCreate.userId() != null && newsCreate.orgId() != null) {
                MyOrg org = manager.find(MyOrg.class, newsCreate.orgId());
                if (org != null && user.getOrgs().contains(org)) {
                    logger.debug("has access so allowed canEditNews (update)");
                    return true;
                }
            }
        } else {
            MyUser user = (MyUser) auth.getPrincipal();
            user = manager.find(MyUser.class, user.getId());
            if (user != null && newsCreate.userId() != null && newsCreate.orgId() != null
                    && newsCreate.userId().equals(user.getId())) {

                MyOrg org = manager.find(MyOrg.class, newsCreate.orgId());
                if (org != null && user.getOrgs().contains(org) ) {
                    logger.debug("has access so allowed canEditNews (new)");
                    return true;
                }
            }

        }
        return false;

    }

    private boolean canDeleteNews(Authentication auth, MyNews news) {
        if (news == null) {
            return false;
        }
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            logger.debug("admin so allowed canDeleteNews");
            return true;
        }
        MyUser user = (MyUser) auth.getPrincipal();
        user = manager.find(MyUser.class, user.getId());
        if (news != null && user != null && user.getOrgs().contains(news.getOrg())) {
            return true;
        }
        return false;
    }
}
