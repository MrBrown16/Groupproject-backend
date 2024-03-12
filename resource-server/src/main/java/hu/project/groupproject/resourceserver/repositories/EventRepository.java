package hu.project.groupproject.resourceserver.repositories;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyEvent;

public interface EventRepository extends JpaRepository<MyEvent, String>{


    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate) FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser WHERE p.id=:id")
    Optional<EventDtoPublic> findEventDtoById(@Param(value = "id") String id);

    //Done create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"

    //name
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
    " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
    "WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Set<EventDtoPublic> findEventDtoByNameLike(@Param(value = "name") String name);
    //description
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
    " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
    "WHERE LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Set<EventDtoPublic> findEventDtoByDescriptionLike(@Param(value = "search") String search);
    //location
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
    " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
    "WHERE LOWER(e.location) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Set<EventDtoPublic> findEventDtoByLocationLike(@Param(value = "search") String search);
    //date
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
    " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
    "WHERE e.startDate <= :timestamp "+
    "AND e.endDate >= TRUNC(:timestamp, 'DD') ")
    Set<EventDtoPublic> findEventDtoByDateBetween(@Param(value = "timestamp") Timestamp timestamp);


}

