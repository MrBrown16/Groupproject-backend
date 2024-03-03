package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hu.project.groupproject.resourceserver.dtos.En.ReservationDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "reservations")
public class MyReservation {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reservation_id")
    String id;

    String preferredName;
    String email;//in case it differs from the registration email
    Integer phone;//in case it differs from the registration phoneNum

    Timestamp startDate;
    Integer minutes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id") 
    @JsonIgnoreProperties("reservations")
    MyUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "org_id") 
    @JsonIgnoreProperties("reservations")
    MyOrg org;


    public MyReservation(ReservationDto reservationDto){
        
    }
// "userId":string
// "orgId":string
// "nev": string
// "email": string
// "telefonszam":number|null
// "startDate": string //yyyy-MM-dd hh:mm
// "endDate": string //yyyy-MM-dd hh:mm

}
