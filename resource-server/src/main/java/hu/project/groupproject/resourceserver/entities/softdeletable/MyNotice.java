package hu.project.groupproject.resourceserver.entities.softdeletable;

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
@Table(name = "notices")
public class MyNotice {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notice_id")
    String id;
    
    String type;
    String urgency;
    String description; 
    String location;
    Integer phone;
    Timestamp date;
    
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id") 
    @JsonIgnoreProperties("notices")
    MyUser user;

}
    