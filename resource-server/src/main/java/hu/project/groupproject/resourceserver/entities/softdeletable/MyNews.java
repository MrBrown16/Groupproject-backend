package hu.project.groupproject.resourceserver.entities.softdeletable;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hu.project.groupproject.resourceserver.enums.NewsTypes;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "news")
public class MyNews{
    
    //just for database filling for the first time (demoController setup)
    public MyNews(MyUser user, MyOrg org, String title, String content, NewsTypes type) {
        this.user = user;
        this.org = org;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "news_id")
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id") //references MyUser.id which has a coloumn name of "user_id"
    @JsonIgnoreProperties("news")
    MyUser user;
    
    @ManyToOne
    @JoinColumn(name = "org_id") //references MyOrg.id which has a coloumn name of "org_id"
    @JsonIgnoreProperties("news")
    MyOrg org;
    // @Lob
    // @Column(name = "title", columnDefinition = "TEXT")
    String title;
    // @Lob
    // @Column(name = "content", columnDefinition = "TEXT")
    String content;

    @Enumerated(EnumType.STRING)
    NewsTypes type;

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
        MyNews other = (MyNews) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}