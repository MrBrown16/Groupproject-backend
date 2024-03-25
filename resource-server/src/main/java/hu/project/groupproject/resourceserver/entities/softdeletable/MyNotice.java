package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hu.project.groupproject.resourceserver.enums.NoticeTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "notices")
public class MyNotice {

    //just for database filling for the first time (demoController setup)
    public MyNotice(NoticeTypes type, String urgency, String description, String location, Long phone, Timestamp date,
            MyUser user) {
        this.type = type;
        this.urgency = urgency;
        this.description = description;
        this.location = location;
        this.phone = phone;
        this.date = date;
        this.user = user;
    }

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notice_id")
    String id;
    
    @Enumerated(EnumType.STRING)
    NoticeTypes type;

    String urgency;
    String description; 
    String location;
    Long phone;
    Timestamp date;
    
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id") 
    @JsonIgnoreProperties("notices")
    MyUser user;

}
    