package hu.project.groupproject.nonSoftDeletableEntities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @RequiredArgsConstructor
@Table(name = "days")
public class DayOfWeek {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_id")
    Long id;

    String name;

}
