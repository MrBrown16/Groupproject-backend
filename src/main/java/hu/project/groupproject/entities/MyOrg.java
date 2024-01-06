package hu.project.groupproject.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class MyOrg {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id")
    Long id;

    String name;
    @Column(name = "logo_path")
    String logoPath;
    String url;

    @ManyToMany(mappedBy = "orgs", fetch=FetchType.LAZY)
    @JsonIgnoreProperties("orgs")
    Set<MyUser> users;

    @OneToMany(mappedBy = "org", fetch=FetchType.LAZY) 
    @Column(name = "posts_fk")
    @JsonIgnoreProperties("user")
    Set<MyPost> posts;

    
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


}
