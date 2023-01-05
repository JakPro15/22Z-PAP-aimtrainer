package z03.pap22z.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;


@RunWith(JUnit4.class)
public class SavedSettingsTest extends TestCase {
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
    public void testReadAllSettings() {
        List<ProfileSettings> profiles = SavedSettings.readAllSettings();
        assertEquals(profiles.size(), 1);
        ProfileSettings defaultProfile = profiles.get(0);
        assertEquals(defaultProfile.getId(), 1);
        assertEquals(defaultProfile.getName(), "default");
        assertEquals(defaultProfile.getMusicVolume(), 50);
        assertEquals(defaultProfile.getSfxVolume(), 50);
        assertEquals(defaultProfile.getGameDifficulty(), 2);
        assertEquals(defaultProfile.getGameLength(), 20);
        assertEquals(defaultProfile.getUser().getProfile(), defaultProfile);
        assertEquals(defaultProfile.getResults().size(), 0);
    }

    @Test
    public void updateAllSettings() {
        List<ProfileSettings> profiles = new ArrayList<ProfileSettings>();
        for(int i = 0; i < 3; i++) {
            ProfileSettings profile = ProfileSettings.getDefaultProfile();
            profile.setId(-1);
            profile.setName(String.format("prof%d", i));
            profiles.add(profile);
        }
        List<Integer> deletedIds = new ArrayList<Integer>();
        deletedIds.add(SavedSettings.readAllSettings().get(0).getId());
        SavedSettings.updateAllSettings(profiles, deletedIds);
        List<ProfileSettings> readProfiles = SavedSettings.readAllSettings();
        assertEquals(readProfiles.size(), 3);
        ProfileSettings profile0 = ProfileSettings.findProfileById(readProfiles, profiles.get(0).getId());
        ProfileSettings profile1 = ProfileSettings.findProfileById(readProfiles, profiles.get(1).getId());
        ProfileSettings profile2 = ProfileSettings.findProfileById(readProfiles, profiles.get(2).getId());
        assertEquals(profile0.getName(), "prof0");
        assertEquals(profile1.getName(), "prof1");
        assertEquals(profile2.getName(), "prof2");
    }

    @Test
    public void testReadWriteCurrentProfile() {
        assertEquals(SavedSettings.readCurrentProfile(), 1);

        List<ProfileSettings> profiles = SavedSettings.readAllSettings();
        for(int i = 0; i < 3; i++) {
            ProfileSettings profile = ProfileSettings.getDefaultProfile();
            profile.setId(-1);
            profile.setName(String.format("prof%d", i));
            profiles.add(profile);
        }
        SavedSettings.updateAllSettings(profiles, new ArrayList<Integer>());
        for(int i = 3; i >= 0; i--) {
            SavedSettings.writeCurrentProfile(profiles.get(i));
            assertEquals(SavedSettings.readCurrentProfile(), profiles.get(i).getId());
        }
    }
}
