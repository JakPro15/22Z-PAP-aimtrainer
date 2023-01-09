package z03.pap22z.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ResultsStatistics")
public class Statistics {
    @Id
    @Column(name = "GameType", nullable = false)
    private String gameType;

    @Column(name = "AverageScore", nullable = false)
    private int averageScore;

    @Column(name = "AverageAccuracy", nullable = false)
    private double averageAccuracy;

    @Column(name = "NumberOfGames", nullable = false)
    private int numberOfGames;
}
