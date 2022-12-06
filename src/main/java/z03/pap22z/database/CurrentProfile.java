package z03.pap22z.database;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CurrentProfile")
public class CurrentProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CurrentProfileID", unique = true, nullable = false)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ProfileID", referencedColumnName = "ProfileID")
    private ProfileSettings profile;

    public ProfileSettings getProfile() {
        return profile;
    }

    public void setProfile(ProfileSettings profile) {
        this.profile = profile;
    }
}