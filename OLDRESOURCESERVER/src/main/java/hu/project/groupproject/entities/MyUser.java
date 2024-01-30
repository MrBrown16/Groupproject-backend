package hu.project.groupproject.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "users")
@NamedEntityGraphs(value = { 
    @NamedEntityGraph(name = "graph.User.public", attributeNodes ={ 
        @NamedAttributeNode(value ="id" ),
        @NamedAttributeNode(value = "userName"),
        @NamedAttributeNode(value = "firstName"),
        @NamedAttributeNode(value = "lastName"),
        @NamedAttributeNode(value = "profileImagePath")
    }),
    @NamedEntityGraph(name = "graph.User.private", attributeNodes = {
        @NamedAttributeNode(value ="id" ),
        @NamedAttributeNode(value = "userName"),
        @NamedAttributeNode(value = "firstName"),
        @NamedAttributeNode(value = "lastName"),
        @NamedAttributeNode(value = "profileImagePath"),
        @NamedAttributeNode(value = "email"),
        @NamedAttributeNode(value = "phone")
    })
})
public class MyUser {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "user_id")
    Long id;
    
    String userName;
    String firstName;
    String lastName;
    Long phone;
    @NaturalId
    String email;
    String profileImagePath;

    @OneToMany(mappedBy = "user", fetch=FetchType.LAZY) // references MyPost.user
    @Column(name = "posts_fk")
    @JsonIgnoreProperties("user")
    Set<MyPost> posts;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orgs_admins", 
        joinColumns = { @JoinColumn( name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "org_id")})
    @JsonIgnoreProperties("users")
    Set<MyOrg> orgs;

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

    
}
