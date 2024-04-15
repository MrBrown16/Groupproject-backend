package hu.project.groupproject.resourceserver.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.management.InvalidAttributeValueException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import hu.project.groupproject.resourceserver.dtos.En.ReservationDto;
import hu.project.groupproject.resourceserver.dtos.En.ReservationDtoPublic;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyOrg;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyReservation;
import hu.project.groupproject.resourceserver.entities.softdeletable.MyUser;
import hu.project.groupproject.resourceserver.repositories.ReservationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {
    protected final Log logger = LogFactory.getLog(getClass());


    @PersistenceContext
    EntityManager manager;

    ReservationRepository reservationRepository;
    OrgService orgService;
    UserService userService;

    public ReservationService(ReservationRepository reservationRepository, OrgService orgService, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.orgService=orgService;
        this.userService = userService;
    }
    
    @Transactional
    public void createReservationByUser(String userId,ReservationDto reservationDto) throws InvalidAttributeValueException{
        logger.debug("outside if canEditReservation(userId,null,null, reservationDto)");
        if (canEditReservation(userId,null,null, reservationDto)) {
            logger.debug("inside if canEditReservation(userId,null,null, reservationDto)");
            MyReservation reservation = new MyReservation();
            reservation = mapReservationDtoToMyReservation(reservation, reservationDto);
            manager.persist(reservation);
            
        }
    }
    @Transactional
    public void updateReservationByUser(String userId,String reservationId, ReservationDto reservationDto) throws InvalidAttributeValueException{
        if (canEditReservation(userId,null, reservationId, reservationDto)) {
            MyReservation reservation = manager.find(MyReservation.class, reservationId);
            reservation = mapReservationDtoToMyReservation(reservation, reservationDto);
            //should save by itself
            manager.persist(reservation);
        }
    }
    @Transactional
    public void deleteReservationByUser(String userId,String reservationId){
        if (canDeleteReservation(userId, null, reservationId)) {
            MyReservation reservation = manager.find(MyReservation.class, reservationId);
            if (reservation != null) {
                reservationRepository.delete(reservation);
            }
        }
    }
    @Transactional
    public void createReservationByOrg(String orgId,ReservationDto reservationDto) throws InvalidAttributeValueException{
        if (canEditReservation(null,orgId,null, reservationDto)) {
            MyReservation reservation = new MyReservation();
            reservation = mapReservationDtoToMyReservation(reservation, reservationDto);
            manager.persist(reservation);
            
        }
    }
    @Transactional
    public void updateReservationByOrg(String orgId,String reservationId, ReservationDto reservationDto) throws InvalidAttributeValueException{
        if (canEditReservation(null,orgId, reservationId, reservationDto)) {
            MyReservation reservation = manager.find(MyReservation.class, reservationId);
            reservation = mapReservationDtoToMyReservation(reservation, reservationDto);
            //should save by itself
            manager.persist(reservation);
        }
    }
    @Transactional
    public void deleteReservationByOrg(String orgId,String reservationId){
        if (canDeleteReservation(null, orgId, reservationId)) {
            MyReservation reservation = manager.find(MyReservation.class, reservationId);
            if (reservation != null) {
                reservationRepository.delete(reservation);
            }
        }
    }

    public Optional<ReservationDtoPublic> getReservation(String reservationId){
        return reservationRepository.findReservationDtoById(reservationId);        
    }
    public Optional<ReservationDtoPublic> getReservation(String userId, String reservationId){
        Optional<ReservationDtoPublic> dto = reservationRepository.findReservationDtoById(reservationId); 
        if (dto.isPresent() && dto.get().userId()== userId) {
            return dto;
        }
        return Optional.empty();
    }



    public Set<ReservationDtoPublic> getReservationsForOrg(String orgId){
        Set<String> reservationIds = orgService.getReservationsIdsForOrg(orgId);
        Set<ReservationDtoPublic> reservations = new HashSet<>();
        for (String reservationId : reservationIds) {
            Optional<ReservationDtoPublic> reservation = getReservation(reservationId);
            if (reservation.isPresent()) {
                reservations.add(reservation.get());
            }
        }
        return reservations;
        
    }
    public Set<ReservationDtoPublic> getReservationsForUser(String userId){
        Set<String> reservationIds = userService.getReservationIdsForUser(userId);
        Set<ReservationDtoPublic> reservations = new HashSet<>();
        for (String reservationId : reservationIds) {
            Optional<ReservationDtoPublic> reservation = getReservation(reservationId);
            if (reservation.isPresent()) {
                reservations.add(reservation.get());
            }
        }
        return reservations;
        
    }


    private boolean canEditReservation(String userId,String orgId, String reservationId, ReservationDto reservationDto){
        if (reservationId != null) {
            MyReservation reservation = manager.find(MyReservation.class, reservationId);
            if (userId != null && reservation != null && reservationDto.userId() != null && reservationDto.userId().equals(userId)) {
                MyUser user = manager.find(MyUser.class, userId);
                if (user != null && reservation.getUser().equals(user)) {
                    return true;
                }
            }
            if (orgId != null && reservation != null && reservationDto.orgId() != null && reservationDto.orgId().equals(orgId)) {
                MyOrg org = manager.find(MyOrg.class, orgId);
                if (org != null && reservation.getOrg().equals(org)) {
                    return true;
                }
            }
        }else{
            if (userId != null && reservationDto.userId() != null && reservationDto.userId().equals(userId)) {
                MyUser user = manager.find(MyUser.class, userId);
                if (user != null) {
                    return true;
                }
            }
            if (orgId != null && reservationDto.orgId() != null && reservationDto.orgId().equals(orgId)) {
                MyOrg org = manager.find(MyOrg.class, orgId);
                if (org != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canDeleteReservation(String userId, String orgId, String reservationId){
        if (reservationId != null) {
            MyReservation reservation;
            if (userId != null) {
                MyUser user = manager.find(MyUser.class, userId);
                reservation = manager.find(MyReservation.class, reservationId);
                if (user != null && reservation != null && reservation.getUser().equals(user)) {
                    return true;
                }
            }else if (orgId != null) {
                reservation = manager.find(MyReservation.class, reservationId);
                MyOrg org = manager.find(MyOrg.class, orgId);
                if (org != null && reservation != null && reservation.getOrg().equals(org)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validInput(ReservationDto dto){
        if (dto.orgId() != null && dto.userId() != null && dto.startDate() != null && dto.endDate() != null) {
            return true;
        }
        return false;
    }

    private MyReservation mapReservationDtoToMyReservation(MyReservation reservation, ReservationDto reservationDto) throws InvalidAttributeValueException{
        if (!validInput(reservationDto)) {
            throw new InvalidAttributeValueException("The given input has missing values");
        }
        MyOrg org = manager.find(MyOrg.class, reservationDto.orgId());
        MyUser user = manager.find(MyUser.class, reservationDto.userId()); 
        if (org != null && user != null) {
            reservation.setOrg(null);
            reservation.setUser(user);
        }else{
            throw new InvalidAttributeValueException("No User or Organisation was found with the provided parameters");
        }
        reservation.setEmail(reservationDto.email());
        reservation.setPhone(reservationDto.phone());
        reservation.setEndDate(reservationDto.endDate());
        reservation.setStartDate(reservationDto.startDate());
        reservation.setPreferredName(reservationDto.preferredName());

        return reservation;
    }
}
