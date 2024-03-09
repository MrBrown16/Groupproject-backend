package hu.project.groupproject.resourceserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.NoticeDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyNotice;

public interface NoticeRepository extends JpaRepository<MyNotice, String>{
    
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.NoticeDtoPublic( n.id, n.user.id, n.type, n.urgency, n.description, n.location, n.phone, n.date) FROM MyNotice n LEFT JOIN n.user WHERE p.id=:id")
    Optional<NoticeDtoPublic> findNoticeDtoById(@Param(value = "id") String id);

}
