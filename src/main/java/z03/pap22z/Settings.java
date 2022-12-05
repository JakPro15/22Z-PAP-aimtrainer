package z03.pap22z;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import z03.pap22z.database.Database;

import z03.pap22z.database.ProfileSettings;


public class Settings {
    private static ProfileSettings currentProfile;
    private static List<ProfileSettings> profiles;
    private static List<Integer> deletedProfileIds;

    public static final Double[] VALID_GAME_SPEEDS = {0.25, 0.5, 1.0, 2.0, 4.0};

    static {
        initialize();
    }

    /**
     * Initialize the settings.
     * Will also reset the settings to loaded from database (or default) if called later.
     */
    private static void initialize() {
        if(Database.isConnected()) {
            readFromDatabase();
        }
        else {
            // can't read from database - create a default profile instead
            profiles = new ArrayList<ProfileSettings>();
            profiles.add(ProfileSettings.getDefaultProfile());
            deletedProfileIds = new ArrayList<Integer>();
        }
        currentProfile = profiles.get(0);
    }

    /**
     * @return current profile name
     */
    public static String getCurrentProfileName() {
        return currentProfile.getName();
    }

    /**
     * Switch profile to the one with the given name.
     * @param profileName name of the profile to switch to
     * @throws IllegalArgumentException when the given profile does not exist
     */
    public static void setCurrentProfile(String profileName) {
        ProfileSettings profile = ProfileSettings.findProfile(profiles, profileName);
        if(profile == null) {
            throw new IllegalArgumentException("Tried to set a nonexistent profile.");
        }
        currentProfile = profile;
        update();
    }

    /**
     * Create a new profile. The name must be different from all current profiles.
     * The new profile is a copy of the current profile.
     * @param newProfileName name of the new profile
     * @throws RuntimeException when the given profile already exists
     */
    public static void addNewProfile(String newProfileName) {
        if(ProfileSettings.findProfile(profiles, newProfileName) != null) {
            throw new RuntimeException("Tried to add an existing profile.");
        }
        ProfileSettings profile = new ProfileSettings();
        profile.setName(newProfileName);
        profile.setMusicVolume(getMusicVolume());
        profile.setSfxVolume(getSfxVolume());
        profile.setGameSpeed(getGameSpeed());
        profile.setGameLength(getGameLength());
        profiles.add(profile);
    }

    /**
     * Delete the given profile. There must be another profile to switch to.
     * @param profile name of the profile to be deleted
     * @throws IllegalArgumentException when the given profile does not exist
     * @throws RuntimeException when the given profile is the only one
     */
    public static void deleteProfile(String profileName) {
        ProfileSettings profile = ProfileSettings.findProfile(profiles, profileName);
        if(profile == null) {
            throw new IllegalArgumentException("Tried to delete a nonexistent profile.");
        }
        if(profiles.size() == 1) {
            throw new RuntimeException("Tried to delete the only profile.");
        }
        profiles.remove(profile);

        // if deleting current profile, switch to 0th profile
        if(currentProfile == profile) {
            currentProfile = profiles.get(0);
            update();
        }
        deletedProfileIds.add(profile.getId());
    }

    /**
     * Get a copy of the list of names of all profiles.
     * @return list of the profile names
     */
    public static List<String> getProfileNames() {
        List<String> profileNames = new ArrayList<String>();
        for(ProfileSettings profile: profiles) {
            profileNames.add(profile.getName());
        }
        return profileNames;
    }

    /**
     * @return music volume for the current profile
     */
    public static Integer getMusicVolume() {
        return currentProfile.getMusicVolume();
    }

    /**
     * Set music volume for the current profile.
     * @param newMusicVolume music volume to be set
     * @throws IllegalArgumentException when the given volume is out of range [0,100] (inclusive)
     */
    public static void setMusicVolume(Integer newMusicVolume) {
        if(newMusicVolume > 100 || newMusicVolume < 0) {
            throw new IllegalArgumentException("Music volume must be between 0 and 100 (inclusive)");
        }
        currentProfile.setMusicVolume(newMusicVolume);
        update();
    }

    /**
     * @return sound effects volume for the current profile
     */
    public static Integer getSfxVolume() {
        return currentProfile.getSfxVolume();
    }

    /**
     * Set sound effects volume for the current profile.
     * @param newSfxVolume music volume to be set
     * @throws IllegalArgumentException when the given volume is out of range [0,100] (inclusive)
     */
    public static void setSfxVolume(Integer newSfxVolume) {
        if(newSfxVolume > 100 || newSfxVolume < 0) {
            throw new IllegalArgumentException("Sfx volume must be between 0 and 100 (inclusive)");
        }
        currentProfile.setSfxVolume(newSfxVolume);
        update();
    }

    /**
     * @return game speed multiplier for the current profile
     */
    public static Double getGameSpeed() {
        return currentProfile.getGameSpeed();
    }

    /**
     * Set game speed multiplier for the current profile.
     * @param newGameSpeed game speed multiplier to be set
     * @throws IllegalArgumentException when the given number is not in Settings.VALID_GAME_SPEEDS
     */
    public static void setGameSpeed(Double newGameSpeed) {
        if(!Arrays.asList(VALID_GAME_SPEEDS).contains(newGameSpeed)) {
            throw new IllegalArgumentException("Invalid game speed modifier value");
        }
        currentProfile.setGameSpeed(newGameSpeed);
    }

    /**
     * @return single game length for the current profile in seconds
     */
    public static Integer getGameLength() {
        return currentProfile.getGameLength();
    }

    /**
     * Set sound effects volume for the current profile.
     * @param newGameLength single game length in seconds to be set
     * @throws IllegalArgumentException when the given game length is out of range [5,60] (inclusive)
     */
    public static void setGameLength(Integer newGameLength) {
        if(newGameLength >  60 || newGameLength < 5) {
            throw new IllegalArgumentException("Single game length must be between 5 and 60 (inclusive)");
        }
        currentProfile.setGameLength(newGameLength);
    }

    /**
     * Writes all changes to settings to the database (if connected).
     */
    public static void writeToDatabase() {
        if(Database.isConnected()) {
            Database.writeAllSettings(profiles, deletedProfileIds);
            deletedProfileIds = new ArrayList<Integer>();
        }
    }

    /**
     * Reads all settings from the database, replacing the current ones
     * (if there are any).
     * If not connected to the database, does nothing.
     */
    public static void readFromDatabase() {
        if(Database.isConnected()) {
            profiles = Database.readAllSettings();
            deletedProfileIds = new ArrayList<Integer>();
            if(currentProfile != null) {
                currentProfile = ProfileSettings.findProfile(profiles, getCurrentProfileName());
            }
            update();
        }
    }

    /**
     * Updates global settings after something is changed.
     */
    private static void update() {
        // music volume and sfx volume will be updated here
    }
}
