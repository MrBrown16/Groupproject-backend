package hu.project.groupproject.resourceserver.entities.softdeletable;

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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "vote_options")
public class MyVoteOption {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_option_id")
    Long id;

    String optionText;
    
    @ColumnDefault("0")
    Long votesNum;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    @JsonIgnoreProperties("myVoteOption")
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
        MyVoteOption other = (MyVoteOption) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
