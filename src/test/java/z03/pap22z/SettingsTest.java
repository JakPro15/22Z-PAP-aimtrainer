package z03.pap22z;

import static org.junit.Assert.assertThrows;

import java.util.List;
import junit.framework.TestCase;

public class SettingsTest extends TestCase {
    public void testAddNewProfile() {
        List<String> currentProfiles = Settings.getProfileNames();
        // Pick a string which for sure isn't a profile yet
        String newProfile = currentProfiles.toString();
        currentProfiles.add(newProfile);
        Settings.addNewProfile(newProfile);
        assertTrue(Settings.getProfileNames().equals(currentProfiles));

        // Another string which for sure isn't a profile yet
        newProfile = currentProfiles.toString();
        currentProfiles.add(newProfile);
        Settings.addNewProfile(newProfile);
        assertEquals(Settings.getProfileNames(), currentProfiles);
    }

    public void testAddDuplicateProfile() {
        // try to add the 0th profile again
        assertThrows(RuntimeException.class, () -> {
            Settings.addNewProfile(Settings.getProfileNames().get(0));
        });
    }

    public void testSetCurrentProfile() {
        String newProfile1 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile1);
        String newProfile2 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile2);

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getCurrentProfile(), newProfile1);

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getCurrentProfile(), newProfile2);

        Settings.setCurrentProfile(Settings.getProfileNames().get(0));
        assertEquals(Settings.getCurrentProfile(), Settings.getProfileNames().get(0));
    }

    public void testSetNonexistentProfile() {
        String nonexistentProfile = Settings.getProfileNames().toString();
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setCurrentProfile(nonexistentProfile);
        });
    }

    public void testDeleteProfile() {
        List<String> currentProfiles = Settings.getProfileNames();
        // Pick a string which for sure isn't a profile yet
        String newProfile1 = currentProfiles.toString();
        currentProfiles.add(newProfile1);
        Settings.addNewProfile(newProfile1);

        currentProfiles.remove(0);
        Settings.deleteProfile(Settings.getProfileNames().get(0));
        assertEquals(currentProfiles, Settings.getProfileNames());
    }

    public void testDeleteCurrentProfile() {
        List<String> currentProfiles = Settings.getProfileNames();
        // Pick a string which for sure isn't a profile yet
        String newProfile1 = currentProfiles.toString();

        Settings.addNewProfile(newProfile1);
        Settings.setCurrentProfile(newProfile1);
        Settings.deleteProfile(newProfile1);

        assertEquals(currentProfiles, Settings.getProfileNames());
        assertEquals(currentProfiles.get(0), Settings.getCurrentProfile());
    }

    public void testDeleteLastProfile() {
        List<String> currentProfiles = Settings.getProfileNames();
        assertThrows(RuntimeException.class, () -> {
            for(String profile: currentProfiles) {
                Settings.deleteProfile(profile);
            }
        });
    }

    public void testDeleteNonexistentProfile() {
        String nonexistentProfile = Settings.getProfileNames().toString();
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.deleteProfile(nonexistentProfile);
        });
    }

    public void testMusicVolume() {
        Settings.setMusicVolume(50);
        assertEquals(Settings.getMusicVolume(), Integer.valueOf(50));
        // New profiles with music volume 50
        String newProfile1 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile1);
        String newProfile2 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile2);

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getMusicVolume(), Integer.valueOf(50));
        Settings.setMusicVolume(0);
        assertEquals(Settings.getMusicVolume(), Integer.valueOf(0));

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getMusicVolume(), Integer.valueOf(50));
        Settings.setMusicVolume(100);
        assertEquals(Settings.getMusicVolume(), Integer.valueOf(100));

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getMusicVolume(), Integer.valueOf(0));
    }

    public void testMusicVolumeIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setMusicVolume(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setMusicVolume(101);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setMusicVolume(200);
        });
    }
}
