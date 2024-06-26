package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hu.project.groupproject.resourceserver.myabstractclasses.LoadableImages;
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
@Table(name = "items")
public class MyItemForSale extends LoadableImages{

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
    String id;

    String name;
    String description;
    String condition;
    String location;
    String email;
    Long phone;
    Long price;
    


    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id") //references MyUser.id which has a coloumn name of "user_id"
    @JsonIgnoreProperties("items")
    MyUser user;

    
    @Override
    public String getPath() {
        logger.debug("MyItemForSale getPath() user.getId(): "+user.getId());
        logger.debug("MyItemForSale getPath() this.user.getId(): "+this.user.getId());
        return "users/"+this.user.getId()+"/items/"+this.id;
    }
    public static String getPath(String userId, String id){
        return "users/"+userId+"/items/"+id;
    }

    @CreationTimestamp
    Timestamp creationTime;
    @UpdateTimestamp
    Timestamp updateTime;

    // "nev": string
    // "leiras": string
    // "allapot": string
    // "kepek": File[]
    // "helyszin": string
    // "telefonszam": number|null
}
