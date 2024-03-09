package hu.project.groupproject.resourceserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.project.groupproject.resourceserver.entities.softdeletable.MyReservation;

public interface ReservationRepository extends JpaRepository<MyReservation, String> {
    
}
