package z03.pap22z.database;
import lombok.Data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Settings")
public class ProfileSettings implements Serializable {
    @Id
    @Column(name = "ProfileID", unique = true, nullable = false)
    private int id;

    @Column(name = "Name", unique = true, nullable = false)
    private String name;

    @Column(name = "MusicVolume", nullable = false)
    private int musicVolume;

    @Column(name = "SFXVolume", nullable = false)
    private int sfxVolume;

    @Column(name = "GameSpeed", nullable = false)
    private double gameSpeed;

    @Column(name = "GameLength", nullable = false)
    private int gameLength;
}
