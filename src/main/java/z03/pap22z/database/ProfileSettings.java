package z03.pap22z.database;

import java.util.List;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Settings")
public class ProfileSettings implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(mappedBy = "profile")
    private CurrentProfile user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(int sfxVolume) {
        this.sfxVolume = sfxVolume;
    }

    public double getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(double gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public int getGameLength() {
        return gameLength;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    /**
     * @return newly created default profile object
     */
    public static ProfileSettings getDefaultProfile() {
        ProfileSettings defaultProfile = new ProfileSettings();
        defaultProfile.setId(0);
        defaultProfile.setName("default");
        defaultProfile.setMusicVolume(50);
        defaultProfile.setSfxVolume(50);
        defaultProfile.setGameSpeed(1.0);
        defaultProfile.setGameLength(20);
        return defaultProfile;
    }

    /**
     * Finds a profile with the given name in the given list
     * @param profiles list of profiles to search
     * @param profileName name of the profile to find
     * @return the found profile; null if not found
     */
    public static ProfileSettings findProfileByName(List<ProfileSettings> profiles, String profileName) {
        for(ProfileSettings profile: profiles) {
            if(profile.name.equals(profileName)) {
                return profile;
            }
        }
        return null;
    }

    /**
     * Finds a profile with the given id in the given list
     * @param profiles list of profiles to search
     * @param profileId id of the profile to find
     * @return the found profile; null if not found
     */
    public static ProfileSettings findProfileById(List<ProfileSettings> profiles, int profileId) {
        for(ProfileSettings profile: profiles) {
            if(profile.id == profileId) {
                return profile;
            }
        }
        return null;
    }

    /**
     * Sets all attributes except ID to other's attributes.
     * @param other profile to copy from
     */
    public void copyFromOther(ProfileSettings other) {
        name = other.getName();
        musicVolume = other.getMusicVolume();
        sfxVolume = other.getSfxVolume();
        gameSpeed = other.getGameSpeed();
        gameLength = other.getGameLength();
    }
}
