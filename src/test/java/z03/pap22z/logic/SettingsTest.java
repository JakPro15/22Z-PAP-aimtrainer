package z03.pap22z.logic;

import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;
import z03.pap22z.Settings;
import z03.pap22z.database.Database;

@RunWith(JUnit4.class)
public class SettingsTest extends TestCase {
    @Before
    public void setup() throws Exception {
        Database.connect();
        Database.resetDatabase();
    }

    @After
    public void cleanup() {
        Database.resetDatabase();
    }

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
        assertEquals(Settings.getCurrentProfileName(), newProfile1);

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getCurrentProfileName(), newProfile2);

        Settings.setCurrentProfile(Settings.getProfileNames().get(0));
        assertEquals(Settings.getCurrentProfileName(), Settings.getProfileNames().get(0));
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
        assertEquals(currentProfiles.get(0), Settings.getCurrentProfileName());
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
    public void testGameDifficulty() {
        Settings.setGameDifficulty(1);
        assertEquals(Settings.getGameDifficulty(), Integer.valueOf(1));
        // New profiles with game difficulty 1
        String newProfile1 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile1);
        String newProfile2 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile2);

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getGameDifficulty(), Integer.valueOf(1));
        Settings.setGameDifficulty(2);
        assertEquals(Settings.getGameDifficulty(), Integer.valueOf(2));

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getGameDifficulty(), Integer.valueOf(1));
        Settings.setGameDifficulty(4);
        assertEquals(Settings.getGameDifficulty(), Integer.valueOf(4));

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getGameDifficulty(), Integer.valueOf(2));
    }

    @Test
    public void testGameDifficultyIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameDifficulty(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameDifficulty(5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setGameDifficulty(20);
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

    @Test
    public void testSharpshooterLength() {
        Settings.setSharpshooterLength(20);
        assertEquals(Settings.getSharpshooterLength(), Integer.valueOf(20));
        // New profiles with single Sharpshooter length 20
        String newProfile1 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile1);
        String newProfile2 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile2);

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getSharpshooterLength(), Integer.valueOf(20));
        Settings.setSharpshooterLength(3);
        assertEquals(Settings.getSharpshooterLength(), Integer.valueOf(3));

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getSharpshooterLength(), Integer.valueOf(20));
        Settings.setSharpshooterLength(40);
        assertEquals(Settings.getSharpshooterLength(), Integer.valueOf(40));

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getSharpshooterLength(), Integer.valueOf(3));
    }

    @Test
    public void testSharpshooterLengthIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setSharpshooterLength(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setSharpshooterLength(101);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setSharpshooterLength(200);
        });
    }

    @Test
    public void testKeys() {
        Settings.setKey(1, "Q");
        Settings.setKey(2, "W");
        Settings.setKey(3, "E");
        Settings.setKey(4, "R");
        assertEquals(Settings.getKeys().size(), 4);
        assertEquals(Settings.getKeys().get(0), "Q");
        assertEquals(Settings.getKeys().get(1), "W");
        assertEquals(Settings.getKeys().get(2), "E");
        assertEquals(Settings.getKeys().get(3), "R");

        // New profiles with keys Q W E R
        String newProfile1 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile1);
        String newProfile2 = Settings.getProfileNames().toString();
        Settings.addNewProfile(newProfile2);

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getKeys().size(), 4);
        assertEquals(Settings.getKeys().get(0), "Q");
        assertEquals(Settings.getKeys().get(1), "W");
        assertEquals(Settings.getKeys().get(2), "E");
        assertEquals(Settings.getKeys().get(3), "R");
        Settings.setKey(2, "5");
        Settings.setKey(4, "Z");
        assertEquals(Settings.getKeys().size(), 4);
        assertEquals(Settings.getKeys().get(0), "Q");
        assertEquals(Settings.getKeys().get(1), "5");
        assertEquals(Settings.getKeys().get(2), "E");
        assertEquals(Settings.getKeys().get(3), "Z");

        Settings.setCurrentProfile(newProfile2);
        assertEquals(Settings.getKeys().size(), 4);
        assertEquals(Settings.getKeys().get(0), "Q");
        assertEquals(Settings.getKeys().get(1), "W");
        assertEquals(Settings.getKeys().get(2), "E");
        assertEquals(Settings.getKeys().get(3), "R");
        Settings.setKey(1, "L");
        Settings.setKey(3, "X");
        assertEquals(Settings.getKeys().size(), 4);
        assertEquals(Settings.getKeys().get(0), "L");
        assertEquals(Settings.getKeys().get(1), "W");
        assertEquals(Settings.getKeys().get(2), "X");
        assertEquals(Settings.getKeys().get(3), "R");

        Settings.setCurrentProfile(newProfile1);
        assertEquals(Settings.getKeys().size(), 4);
        assertEquals(Settings.getKeys().get(0), "Q");
        assertEquals(Settings.getKeys().get(1), "5");
        assertEquals(Settings.getKeys().get(2), "E");
        assertEquals(Settings.getKeys().get(3), "Z");
    }

    @Test
    public void testKeysIllegalValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setKey(0, "A");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setKey(5, "A");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Settings.setKey(100, "A");
        });
    }
}
