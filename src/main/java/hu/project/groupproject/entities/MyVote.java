package hu.project.groupproject.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "votes")
public class MyVote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    Long id;

    String description;

    @OneToMany(mappedBy = "vote", fetch=FetchType.LAZY)
    @Column(name = "vote_fk")
    @JsonIgnoreProperties("vote")
    List<MyVoteOption> options;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties("vote")
    MyPost post;

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
        MyVote other = (MyVote) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


}
