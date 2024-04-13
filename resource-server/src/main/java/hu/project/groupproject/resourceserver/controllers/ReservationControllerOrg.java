package hu.project.groupproject.resourceserver.controllers;

import java.util.Optional;
import java.util.Set;

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
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyReservation;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.services.ReservationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@RequestMapping("org/reservation")
public class ReservationControllerOrg {
     protected final Log logger = LogFactory.getLog(getClass());

    @PersistenceContext
    EntityManager manager;

    ReservationService reservationService;

    
    public ReservationControllerOrg(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public Optional<ReservationDtoPublic> getReservation(@PathVariable String reservationId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        MyReservation reservation = manager.find(MyReservation.class, reservationId);
        if (reservation != null) {
            MyOrg org = manager.find(MyOrg.class, reservation.getOrg().getId());
            if (user != null && org != null && user.getOrgs().contains(org)) {
                return reservationService.getReservation(reservationId);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/sajat/{orgId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public Set<ReservationDtoPublic> getReservationsForOrg(@PathVariable String orgId, Authentication auth) {
        MyUser user = (MyUser)auth.getPrincipal();
        MyOrg org = manager.find(MyOrg.class, orgId);
        if (user != null && org != null && org.getUsers().contains(user)) {
            return reservationService.getReservationsForOrg(orgId);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void saveReservation(@RequestBody ReservationDto reservation, Authentication auth){
        MyUser user = (MyUser)auth.getPrincipal();
        MyOrg org = manager.find(MyOrg.class, reservation.orgId());
        if (user != null && org != null && user.getOrgs().contains(org)) {
            try {
                reservationService.createReservationByOrg(org.getId(),reservation);
            } catch (InvalidAttributeValueException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
            }
        }
    }

    @PutMapping("/{reservationId}") 
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void updateReservation(@PathVariable String reservationId,@RequestBody ReservationDto reservation, Authentication auth) throws NotFoundException{
        MyUser user = (MyUser)auth.getPrincipal();
        MyOrg org = manager.find(MyOrg.class, reservation.orgId());
        if (user != null && org != null && user.getOrgs().contains(org)) {
            try {
                reservationService.updateReservationByOrg(org.getId(),reservationId,reservation);
            } catch (InvalidAttributeValueException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
            }
        }
    }
    
    @DeleteMapping("/del/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ADMIN')")
    public void deleteReservation(@PathVariable String reservationId, Authentication auth) {
        if (reservationId==null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        MyReservation reservation = manager.find(MyReservation.class, reservationId);
        if (reservation != null) {
            
            MyUser user = (MyUser)auth.getPrincipal();
            MyOrg org = manager.find(MyOrg.class, reservation.getOrg().getId());
            if (user != null && org != null && user.getOrgs().contains(org)) {
                reservationService.deleteReservationByOrg(org.getId(), reservationId);
            }
        }
    }
}
