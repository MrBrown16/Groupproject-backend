package hu.project.groupproject.resourceserver.controllers;

import java.util.Optional;

import javax.management.InvalidAttributeValueException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.project.groupproject.resourceserver.dtos.En.ReservationDto;
import hu.project.groupproject.resourceserver.dtos.En.ReservationDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.ReservationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@RequestMapping("user/reservation")
public class ReservationControllerUser {
     protected final Log logger = LogFactory.getLog(getClass());

    @PersistenceContext
    EntityManager manager;

    ReservationService reservationService;

    
    public ReservationControllerUser(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    public Optional<ReservationDtoPublic> getReservation(@PathVariable String reservationId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null) {
            return reservationService.getReservation(user.getId(),reservationId);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('USER')")
    public void saveReservation(@RequestBody ReservationDto reservation, Authentication auth){
        MyUser user = (MyUser)auth.getPrincipal();
            try {
                logger.debug("Reservation: "+reservation.toString()+" User: "+user.toString());
                reservationService.createReservationByUser(user.getId(),reservation);
            } catch (InvalidAttributeValueException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
            }
        
    }

    @PutMapping("/{reservationId}") 
    @PreAuthorize("hasRole('USER')")
    public void updateReservation(@PathVariable String reservationId,@RequestBody ReservationDto reservation, Authentication auth) throws NotFoundException{
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null) {
            try {
                reservationService.updateReservationByUser(user.getId(),reservationId,reservation);
            } catch (InvalidAttributeValueException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
            }
        }
    }
    
    @DeleteMapping("/del/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteReservation(@PathVariable String reservationId, Authentication auth) {
        if (reservationId==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }            
        MyUser user = (MyUser)auth.getPrincipal();
        if (user != null) {
            reservationService.deleteReservationByUser(user.getId(), reservationId);
        }
        
    }
}
