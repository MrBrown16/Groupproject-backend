package hu.project.groupproject.resourceserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyEvent;

public interface EventRepository extends JpaRepository<MyEvent, String>{


    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate) FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser WHERE p.id=:id")
    Optional<EventDtoPublic> findEventDtoById(@Param(value = "id") String id);


}

