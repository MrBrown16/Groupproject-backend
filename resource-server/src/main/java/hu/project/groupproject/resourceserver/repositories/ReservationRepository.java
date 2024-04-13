package hu.project.groupproject.resourceserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hu.project.groupproject.resourceserver.dtos.En.ReservationDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyReservation;

public interface ReservationRepository extends JpaRepository<MyReservation, String> {
 
    @Query("SELECT new hu.project.groupproject.resourceserver.dtos.En.ReservationDtoPublic(r.id, r.user.id, r.org.id, r.preferredName, r.email, r.phone, r.startDate, r.endDate) FROM MyReservation r LEFT JOIN r.user LEFT JOIN r.org WHERE r.id=:id")
    Optional<ReservationDtoPublic> findReservationDtoById(@Param(value = "id") String id);
    

}
