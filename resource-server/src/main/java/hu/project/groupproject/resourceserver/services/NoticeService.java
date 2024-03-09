package hu.project.groupproject.resourceserver.services;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.repositories.NoticeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class NoticeService {
    


    @PersistenceContext
    EntityManager manager;


    NoticeRepository noticeRepository;


    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }


    
}
