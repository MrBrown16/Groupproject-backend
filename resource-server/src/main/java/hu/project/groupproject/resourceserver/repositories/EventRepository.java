package hu.project.groupproject.resourceserver.repositories;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyEvent;

public interface EventRepository extends JpaRepository<MyEvent, String>{


    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate) FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser WHERE e.id=:id")
    Optional<EventDtoPublic> findEventDtoById(@Param(value = "id") String id);

    //Done create search function starting point:
    //"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))"

    //name
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
    " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
    "WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    Page<EventDtoPublic> findEventDtoByNameLike(@Param(value = "name") String name, Pageable pageable);
    //description
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
    " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
    "WHERE LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Page<EventDtoPublic> findEventDtoByDescriptionLike(@Param(value = "search") String search, Pageable pageable);
    //location
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
    " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
    "WHERE LOWER(e.location) LIKE LOWER(CONCAT('%', :search, '%')) ")
    Page<EventDtoPublic> findEventDtoByLocationLike(@Param(value = "search") String search, Pageable pageable);
    // //date
    // @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
    // " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
    // "WHERE e.startDate <= :timestamp "+
    // "AND e.endDate >= TRUNC(:timestamp, 'DD') ")
    // Page<EventDtoPublic> findEventDtoByDateBetween(@Param(value = "timestamp") Timestamp timestamp, Pageable pageable);

    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.EventDtoPublic( e.id, e.name, e.description, e.location, e.organiserUser.id, e.organiser.id, e.publicPhones, e.publicEmails, e.startDate, e.endDate)"+
            " FROM MyEvent e LEFT JOIN e.organiserUser LEFT JOIN e.organiser "+
            "WHERE e.startDate <= :timestamp "+
            "AND e.endDate >= :truncatedTimestamp ")
    Page<EventDtoPublic> findEventDtoByDateBetween(@Param(value = "timestamp") Timestamp timestamp,
                                                    @Param(value = "truncatedTimestamp") Timestamp truncatedTimestamp,
                                                    Pageable pageable);

    default Page<EventDtoPublic> findEventDtoByDateBetweenTruncatedToDay(Timestamp timestamp, Pageable pageable) {
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        LocalDateTime truncatedDateTime = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
        Timestamp truncatedTimestamp = Timestamp.valueOf(truncatedDateTime);
        return findEventDtoByDateBetween(timestamp, truncatedTimestamp, pageable);
    }
}

