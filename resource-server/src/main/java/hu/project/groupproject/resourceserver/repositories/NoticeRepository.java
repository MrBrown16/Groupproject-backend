package hu.project.groupproject.resourceserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyNotice;

public interface NoticeRepository extends JpaRepository<MyNotice, String>{
    
}
