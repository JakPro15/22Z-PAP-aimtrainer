package z03.pap22z;

import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

public class SettingsTest extends TestCase {
    @Test
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

    @Test
    public void testAddDuplicateProfile() {
        // try to add the 0th profile again
        assertThrows(RuntimeException.class, () -> {
            Settings.addNewProfile(Settings.getProfileNames().get(0));
        });
    }

    @Test
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

    @Test
    public void testSetNonexistentProfile() {
        String nonexistentProfile = Settings.getProfileNames().toString();
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setCurrentProfile(nonexistentProfile);
        });
    }

    @Test
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

    @Test
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

    @Test
    public void testDeleteLastProfile() {
        List<String> currentProfiles = Settings.getProfileNames();
        assertThrows(RuntimeException.class, () -> {
            for(String profile: currentProfiles) {
                Settings.deleteProfile(profile);
            }
        });
    }

    @Test
    public void testDeleteNonexistentProfile() {
        String nonexistentProfile = Settings.getProfileNames().toString();
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.deleteProfile(nonexistentProfile);
        });
    }

    @Test
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

    @Test
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

    @Test
    public void testSfxVolume() {
        Settings.setSfxVolume(50);
        assertEquals(Settings.getSfxVolume(), Integer.valueOf(50));
        // New profiles with sfx volume 50
        String newProfile1 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile1);
        String newProfile2 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile2);

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getSfxVolume(), Integer.valueOf(50));
        Settings.setSfxVolume(0);
        assertEquals(Settings.getSfxVolume(), Integer.valueOf(0));

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getSfxVolume(), Integer.valueOf(50));
        Settings.setSfxVolume(100);
        assertEquals(Settings.getSfxVolume(), Integer.valueOf(100));

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getSfxVolume(), Integer.valueOf(0));
    }

    @Test
    public void testSfxVolumeIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setSfxVolume(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setSfxVolume(101);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setSfxVolume(200);
        });
    }

    @Test
    public void testGameSpeed() {
        Settings.setGameSpeed(0.5);
        assertEquals(Settings.getGameSpeed(), Double.valueOf(0.5));
        // New profiles with game speed multiplier 0.5
        String newProfile1 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile1);
        String newProfile2 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile2);

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getGameSpeed(), Double.valueOf(0.5));
        Settings.setGameSpeed(1.0);
        assertEquals(Settings.getGameSpeed(), Double.valueOf(1.0));

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getGameSpeed(), Double.valueOf(0.5));
        Settings.setGameSpeed(4.0);
        assertEquals(Settings.getGameSpeed(), Double.valueOf(4.0));

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getGameSpeed(), Double.valueOf(1.0));
    }

    @Test
    public void testGameSpeedIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameSpeed(0.0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameSpeed(0.7);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameSpeed(4.5);
        });
    }

    @Test
    public void testGameLength() {
        Settings.setGameLength(20);
        assertEquals(Settings.getGameLength(), Integer.valueOf(20));
        // New profiles with single game length 20
        String newProfile1 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile1);
        String newProfile2 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile2);

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getGameLength(), Integer.valueOf(20));
        Settings.setGameLength(5);
        assertEquals(Settings.getGameLength(), Integer.valueOf(5));

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getGameLength(), Integer.valueOf(20));
        Settings.setGameLength(60);
        assertEquals(Settings.getGameLength(), Integer.valueOf(60));

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getGameLength(), Integer.valueOf(5));
    }

    @Test
    public void testGameLengthIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameLength(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameLength(101);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameLength(200);
        });
    }
}
