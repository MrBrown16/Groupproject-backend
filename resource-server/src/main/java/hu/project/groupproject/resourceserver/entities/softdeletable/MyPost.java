package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "posts")
public class MyPost {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    Long id;

    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id") //references MyUser.id which has a coloumn name of "user_id"
    @JsonIgnoreProperties("posts")
    MyUser user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id") //references MyOrg.id which has a coloumn name of "org_id"
    @JsonIgnoreProperties("posts")
    MyOrg org;

    String content;
    String url;
    List<String> imagePath; 
    @ColumnDefault("0")
    Long likes;
    @ColumnDefault("0")
    Long dislikes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    @JsonIgnoreProperties("post")
    MyVote vote;

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
        MyPost other = (MyPost) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}