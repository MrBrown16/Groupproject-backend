package hu.project.groupproject.resourceserver.services;

import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.repositories.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ReservationService {
    

    @PersistenceContext
    EntityManager manager;

    ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


}
