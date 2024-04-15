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

      
    //just for database filling for the first time (demoController setup)
    public MyEvent(String name, String description, String location, List<Long> publicPhones,
            List<String> publicEmails, Timestamp startDate, Timestamp endDate, MyOrg organiser, MyUser organiserUser) throws Exception {
        this.name = name;
        this.description = description;
        this.location = location;
        this.publicPhones = publicPhones;
        this.publicEmails = publicEmails;
        this.startDate = startDate;
        this.endDate = endDate;
        this.organiser = organiser;
        this.organiserUser = organiserUser;
    }


    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id")
    String id;

    String name;
    String description;
    String location;
    List<Long> publicPhones;
    List<String> publicEmails;

    Timestamp startDate;
    Timestamp endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id") //references MyOrg.id which has a coloumn name of "org_id"
    @JsonIgnoreProperties("events")
    MyOrg organiser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //references MyUser.id which has a coloumn name of "user_id"
    @JsonIgnoreProperties("events")
    MyUser organiserUser;

}
