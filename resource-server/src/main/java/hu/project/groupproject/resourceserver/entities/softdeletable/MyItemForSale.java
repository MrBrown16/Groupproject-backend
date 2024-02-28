package hu.project.groupproject.resourceserver.entities.softdeletable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hu.project.groupproject.resourceserver.interfaces.LoadableImages;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;

    String name;
    String description;
    String condition;
    String location;
    Integer phone;
    
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id") //references MyUser.id which has a coloumn name of "user_id"
    @JsonIgnoreProperties("items")
    MyUser user;
    
    
    @Override
    public String getPath() {
        return "users/"+this.user.getId()+"/items/"+this.id;
    }

    // load images from file system and return them (userid/itemid/)
    // public List<File> getPictures(){
    // }

    // "nev": string
    // "leiras": string
    // "allapot": string
    // "kepek": File[]
    // "helyszin": string
    // "telefonszam": number|null
}
