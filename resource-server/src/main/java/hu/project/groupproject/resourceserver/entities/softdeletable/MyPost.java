package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "posts")
public class MyPost extends LoadableImages{
    
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "post_id")
    String id;

    @ManyToOne(optional = false, fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id") //references MyUser.id which has a coloumn name of "user_id"
    @JsonIgnoreProperties("posts")
    MyUser user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id") //references MyOrg.id which has a coloumn name of "org_id"
    @JsonIgnoreProperties("posts")
    MyOrg org;

    String content;

    @ColumnDefault("0")
    Long likes;
    @ColumnDefault("0")
    Long dislikes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    @JsonIgnoreProperties("post")
    MyVote vote;


    @CreationTimestamp
    Timestamp creationTime;
    @UpdateTimestamp
    Timestamp updateTime;


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

    //TODO: make it possible for org admins to post in their own name
    @Override
    public String getPath() {
        if (this.org==null) {
            return "users/"+this.user.getId()+"/posts/"+this.id;
        }else{
            return "orgs/"+this.org.getId()+"/posts/"+this.id;
        }
    }
    public static String getPath(String entityId, String postId, boolean isOrg) {
        if (!isOrg) {
            return "users/"+entityId+"/posts/"+postId;
        }else{
            return "orgs/"+entityId+"/posts/"+postId;
        }
    }


}