package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hu.project.groupproject.resourceserver.enums.NoticeTypes;
import hu.project.groupproject.resourceserver.enums.OrgCategory;
import hu.project.groupproject.resourceserver.myabstractclasses.LoadableImages;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "organisations")
public class MyOrg extends LoadableImages{
    
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "org_id")
    String id;

    String name;

    @ManyToMany(mappedBy = "orgs", fetch=FetchType.LAZY)
    @JsonIgnoreProperties("orgs")
    Set<MyUser> users;

    @OneToMany(mappedBy = "org", fetch=FetchType.LAZY) 
    @Column(name = "posts_fk")
    @JsonIgnoreProperties("org")
    Set<MyPost> posts;

    @OneToMany(mappedBy = "org", fetch=FetchType.LAZY) 
    @Column(name = "news_fk")
    @JsonIgnoreProperties("org")
    Set<MyNews> news;
    
    @OneToMany(mappedBy = "org", fetch=FetchType.LAZY) 
    @Column(name = "reservation_fk")
    @JsonIgnoreProperties("org")
    Set<MyReservation> reservations;

    @OneToMany(mappedBy = "organiser", fetch=FetchType.LAZY) 
    @Column(name = "events_fk")
    @JsonIgnoreProperties("organiser")
    Set<MyEvent> events;

    @ElementCollection(targetClass = OrgCategory.class)
    @Enumerated(EnumType.STRING)
    Set<OrgCategory> categories;

    @ElementCollection(targetClass = NoticeTypes.class)
    @Enumerated(EnumType.STRING)
    Set<NoticeTypes> responsibilities;

    
    public void setUsers(Set<MyUser> users) {
        if (this.users == null) {
            this.users = users;
        } else {
            if (users != null) {
                for (MyUser opt : users) {
                    addUser(opt);
                }
            }
        }
    }
    public void setResponsibilities(Set<MyUser> users) {
        if (this.users == null) {
            this.users = users;
        } else {
            if (users != null) {
                for (MyUser opt : users) {
                    addUser(opt);
                }
            }
        }
    }

    public void addUser(MyUser user) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
    
        if (!this.users.contains(user)) {
            this.users.add(user);
            
            if (!user.getOrgs().contains(this)) {
                user.addOrg(this);
            }
        }
    }
    public void addCategory(OrgCategory category) {
        if (this.categories == null) {
            this.categories = new HashSet<>();
        }
    
        if (!this.categories.contains(category)) {
            this.categories.add(category);
        }
    }
    public void removeCategory(OrgCategory category) {
        if (this.categories != null && this.categories.contains(category)) {
            this.categories.remove(category);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        MyOrg other = (MyOrg) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String getPath() {
        return "orgs/"+this.id+"/logo/current";
    }
    public static String getPath(String id) {
        return "orgs/"+id+"/logo/current";
    }
    
}
