package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.util.List;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "events")
public class MyEvent {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long id;

    String name;
    String description;
    String location;
    List<Integer> publicPhones;
    List<String> publicEmails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id") //references MyOrg.id which has a coloumn name of "org_id"
    @JsonIgnoreProperties("events")
    MyOrg organiser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //references MyUser.id which has a coloumn name of "user_id"
    @JsonIgnoreProperties("events")
    MyUser organiserUser;

    Timestamp startDate;
    Timestamp endDate;
// "nev": string
// "leiras": string
// "helyszin": string
// "userid": number
// "orgid": number
// "orgnev": string
// "idopont": {"startDate": string, "endDate": string}
// "telefonszam":Array<number> 
// "email": Array<string> 


}
