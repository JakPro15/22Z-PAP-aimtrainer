package z03.pap22z.database;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "Results")
public class Result implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    @Column(name = "ResultID", unique = true, nullable = false)
    private int id;

    @Column(name = "GameTime", nullable = false)
    private LocalDateTime gameTime;

    @Column(name = "GameDifficulty", nullable = false)
    private int gameDifficulty;

    @Column(name = "GameLength", nullable = false)
    private int gameLength;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "ProfileID", referencedColumnName = "ProfileID")
    private ProfileSettings profile;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "StatResultID", referencedColumnName = "StatResultID")
    private StatResult statResult;

    public int getScore() {
        return getStatResult().getScore();
    }

    public void setScore(int score) {
        getStatResult().setScore(score);
    }

    public double getAccuracy() {
        return getStatResult().getAccuracy();
    }

    public void setAccuracy(double accuracy) {
        getStatResult().setAccuracy(accuracy);
    }

    public String getGameType() {
        return getStatResult().getGameType();
    }

    public void setGameType(String gameType) {
        getStatResult().setGameType(gameType);
    }
}
