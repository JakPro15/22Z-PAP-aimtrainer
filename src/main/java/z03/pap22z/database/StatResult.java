package z03.pap22z.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "StatResults")
public class StatResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    @Column(name = "StatResultID", unique = true, nullable = false)
    private int id;

    @Column(name = "Score", nullable = false)
    private int score;

    @Column(name = "Accuracy", nullable = false)
    private double accuracy;

    @Column(name = "GameType", nullable = false)
    private String gameType;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "statResult")
    private Result result;
}
