package z03.pap22z.database;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class ProfileSettingsTest {
    @Test
    public void testFindProfileByName() {
        List<ProfileSettings> profiles = new ArrayList<ProfileSettings>();
        for(int i = 0; i < 5; i++)
        {
            ProfileSettings profile = ProfileSettings.getDefaultProfile();
            profile.setName(String.format("prof%d", i));
            profiles.add(profile);
        }
        assertEquals(ProfileSettings.findProfileByName(profiles, "prof0"), profiles.get(0));
        assertEquals(ProfileSettings.findProfileByName(profiles, "prof1"), profiles.get(1));
        assertEquals(ProfileSettings.findProfileByName(profiles, "prof2"), profiles.get(2));
        assertEquals(ProfileSettings.findProfileByName(profiles, "prof3"), profiles.get(3));
        assertEquals(ProfileSettings.findProfileByName(profiles, "prof4"), profiles.get(4));
    }

    @Test
    public void testFindProfileById() {
        List<ProfileSettings> profiles = new ArrayList<ProfileSettings>();
        for(int i = 4; i >= 0; i--)
        {
            ProfileSettings profile = ProfileSettings.getDefaultProfile();
            profile.setId(i);
            profiles.add(profile);
        }
        assertEquals(ProfileSettings.findProfileById(profiles, 0), profiles.get(4));
        assertEquals(ProfileSettings.findProfileById(profiles, 1), profiles.get(3));
        assertEquals(ProfileSettings.findProfileById(profiles, 2), profiles.get(2));
        assertEquals(ProfileSettings.findProfileById(profiles, 3), profiles.get(1));
        assertEquals(ProfileSettings.findProfileById(profiles, 4), profiles.get(0));
    }

    @Test
    public void testCopyFromOther() {
        ProfileSettings profile1 = ProfileSettings.getDefaultProfile();
        ProfileSettings profile2 = new ProfileSettings();
        profile2.copyFromOther(profile1);
        assertEquals(profile1.getName(), profile2.getName());
        assertEquals(profile1.getMusicVolume(), profile2.getMusicVolume());
        assertEquals(profile1.getSfxVolume(), profile2.getSfxVolume());
        assertEquals(profile1.getGameDifficulty(), profile2.getGameDifficulty());
        assertEquals(profile1.getGameLength(), profile2.getGameLength());
    }
}
