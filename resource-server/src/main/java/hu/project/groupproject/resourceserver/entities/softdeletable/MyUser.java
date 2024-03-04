package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hu.project.groupproject.resourceserver.myabstractclasses.LoadableImages;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "users")
public class MyUser extends LoadableImages{
    
    @Id @GeneratedValue(strategy = GenerationType.UUID) 
    @Column(name = "user_id")
    String id;
    
    @NaturalId //i hate this, but i would need to redo all the authorization server userhandlings
    String userName;
    String firstName;
    String lastName;
    Integer phone;

    @NaturalId
    String email;
    
    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY) // references MyPost.user
    @Column(name = "posts_fk")
    @JsonIgnoreProperties("user")
    Set<MyPost> posts;
    
    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY) // references MyPost.user
    @Column(name = "reservation_fk")
    @JsonIgnoreProperties("user")
    Set<MyReservation> reservations;
    
    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY)
    @Column(name = "notice_fk")
    @JsonIgnoreProperties("user")
    Set<MyNotice> notices;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orgs_admins", 
        joinColumns = { @JoinColumn( name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "org_id")})
    @JsonIgnoreProperties("users")
    Set<MyOrg> orgs;

    @OneToMany(mappedBy = "organiserUser", fetch=FetchType.LAZY) 
    @Column(name = "events_fk")
    @JsonIgnoreProperties("organiserUser")
    Set<MyEvent> events;

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY) // references MyPost.user
    @Column(name = "item_fk")
    @JsonIgnoreProperties("user")
    Set<MyItemForSale> items;


    public void setOrgs(Set<MyOrg> orgs) {
        if (this.orgs == null) {
            this.orgs = orgs;
        } else {
            if (orgs != null) {
                for (MyOrg opt : orgs) {
                    addOrg(opt);
                }
            }
        }
    }

    public void addOrg(MyOrg org) {
        if (this.orgs == null) {
            this.orgs = new HashSet<>();
        }
    
        if (!this.orgs.contains(org)) {
            this.orgs.add(org);
            
            if (!org.users.contains(this)) {
                org.addUser(this);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MyUser other = (MyUser) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    @Override
    public String getPath() {
        return "users/"+1+"/profile/current";
        // return "users/"+this.id+"/profile/current";
    }

    
}
