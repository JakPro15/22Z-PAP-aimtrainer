package z03.pap22z;

import java.util.ArrayList;
import java.util.List;


public class Settings {
    private static Integer currentProfile;
    private static ArrayList<String> profileNames = new ArrayList<String>();

    private static ArrayList<Integer> musicVolumes = new ArrayList<Integer>();

    static {
        initialize();
    }

    /**
     * Initialize the settings.
     */
    private static void initialize() {
        // temporary values
        // in the end this will load data from the last active profile from the database
        currentProfile = 0;
        profileNames.add("default");
        musicVolumes.add(50);
    }

    /**
     * @return current profile name
     */
    public static String getCurrentProfile() {
        return profileNames.get(currentProfile);
    }

    /**
     * Switch profile to the one with the given name.
     *
     * @param profile name of the profile to switch to
     *
     * @throws IllegalArgumentException when the given profile does not exist
     */
    public static void setCurrentProfile(String profile) {
        int profileNameIndex = profileNames.indexOf(profile);
        if(profileNameIndex == -1) {
            throw new IllegalArgumentException("Tried to set a profile with an invalid name.");
        }
        currentProfile = profileNameIndex;
    }

    /**
     * Create a new profile. The name must be different from all current profiles.
     *
     * @param newProfile name of the new profile
     *
     * @throws RuntimeException when the given profile already exists
     */
    public static void addNewProfile(String newProfile) {
        if(profileNames.contains(newProfile)) {
            throw new RuntimeException("Tried to add an existing profile.");
        }
        profileNames.add(newProfile);
        musicVolumes.add(musicVolumes.get(currentProfile));
    }

    /**
     * Delete the given profile. There must be another profile to switch to.
     *
     * @param profile name of the profile to be deleted
     *
     * @throws IllegalArgumentException when the given profile does not exist
     * @throws RuntimeException when the given profile is the only one
     */
    public static void deleteProfile(String profile) {
        int deletedIndex = profileNames.indexOf(profile);
        if(deletedIndex == -1) {
            throw new IllegalArgumentException("Tried to delete a nonexistent profile.");
        }
        if(profileNames.size() == 1) {
            throw new RuntimeException("Tried to delete the only profile.");
        }
        // save current profile name; in case the index changes
        String currentProfileName = profileNames.get(currentProfile);

        profileNames.remove(deletedIndex);
        musicVolumes.remove(deletedIndex);

        // update currentProfile so that getCurrentProfile works
        if(currentProfileName.equals(profile)) {
            // if deleting current profile, switch to 0th profile
            currentProfile = 0;
        }
        else {
            currentProfile = profileNames.indexOf(currentProfileName);
        }
        setCurrentProfile(profileNames.get(currentProfile));
    }

    /**
     * Get a copy of the list of names of all profiles.
     *
     * @return list of the profile names
     */
    public static List<String> getProfileNames() {
        return new ArrayList<String>(profileNames);
    }

    /**
     * @return music volume for the current profile
     */
    public static Integer getMusicVolume() {
        return musicVolumes.get(currentProfile);
    }

    /**
     * Set music volume for the current profile.
     *
     * @param newMusicVolume music volume to be set
     *
     * @throws IllegalArgumentException when the given volume is out of range [0,100] (inclusive)
     */
    public static void setMusicVolume(Integer newMusicVolume) {
        if(newMusicVolume > 100 || newMusicVolume < 0) {
            throw new IllegalArgumentException("Music volume must be between");
        }
        musicVolumes.set(currentProfile, newMusicVolume);
    }
}
