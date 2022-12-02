package z03.pap22z;

import java.util.ArrayList;
import java.util.List;


enum ProfileValidity {
    VALID, UPDATED, CREATED;
}


public class Settings {
    private static SettingsController settingsController = null;

    private static Integer currentProfile;
    private static ArrayList<String> profileNames = new ArrayList<String>();
    private static ArrayList<ProfileValidity> profileValidities = new ArrayList<ProfileValidity>();
    private static ArrayList<Integer> removedProfiles = new ArrayList<Integer>();

    private static ArrayList<Integer> musicVolumes = new ArrayList<Integer>();

    static {
        // temporary values
        // in the end this will load data from the last active profile
        currentProfile = 0;
        profileNames.add("default");
        profileValidities.add(ProfileValidity.VALID);
        musicVolumes.add(50);
    }

    public static void setSettingsController(SettingsController controller) {
        settingsController = controller;
    }

    public static String getCurrentProfile() {
        return profileNames.get(currentProfile);
    }

    public static void setCurrentProfile(String profile) {
        int profileNameIndex = profileNames.indexOf(profile);
        if(profileNameIndex == -1) {
            throw new RuntimeException("Tried to set a profile with an invalid name.");
        }
        currentProfile = profileNameIndex;
        settingsController.update();
    }

    public static void addNewProfile(String newProfile) {
        if(profileNames.contains(newProfile)) {
            throw new RuntimeException("Tried to add an existing profile.");
        }
        profileNames.add(newProfile);
        profileValidities.add(ProfileValidity.CREATED);
        musicVolumes.add(musicVolumes.get(currentProfile));
        settingsController.profileListChanged();
    }

    public static void deleteProfile(String profile) {
        if(profileNames.size() == 1) {
            throw new RuntimeException("Tried to delete the only profile.");
        }
        int deletedIndex = profileNames.indexOf(profile);
        if(deletedIndex == -1) {
            throw new IllegalArgumentException("Tried to delete a nonexistent profile.");
        }
        // save current profile name; in case the index changes
        String currentProfileName = profileNames.get(currentProfile);
        profileNames.remove(deletedIndex);
        profileValidities.remove(deletedIndex);
        removedProfiles.add(deletedIndex);

        musicVolumes.remove(deletedIndex);

        // update currentProfile so that getCurrentProfile works
        if(currentProfileName.equals(profile)) {
            // if deleting current profile, switch to 0th profile
            currentProfile = 0;
        }
        else {
            currentProfile = profileNames.indexOf(currentProfileName);
        }
        settingsController.profileListChanged();
        setCurrentProfile(profileNames.get(currentProfile));
    }

    public static List<String> getProfileNames() {
        return profileNames;
    }

    public static Integer getMusicVolume() {
        System.out.println(String.format("returning %s", musicVolumes.get(currentProfile)));
        return musicVolumes.get(currentProfile);
    }

    public static void setMusicVolume(Integer newMusicVolume) {
        if(newMusicVolume > 100 || newMusicVolume < 0) {
            throw new IllegalArgumentException("Music volume must be between");
        }
        musicVolumes.set(currentProfile, newMusicVolume);
        profileValidities.set(currentProfile, ProfileValidity.UPDATED);
    }
}
