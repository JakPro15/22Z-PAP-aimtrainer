package z03.pap22z;

import java.util.ArrayList;
import java.util.List;

import z03.pap22z.database.Database;
import z03.pap22z.database.ProfileSettings;
import z03.pap22z.database.SavedSettings;

public class Settings {
    private static ProfileSettings currentProfile;
    private static List<ProfileSettings> profiles;
    private static List<Integer> deletedProfileIds;

    public static final String[] DIFFICULTIES = { "Very Easy", "Easy", "Normal", "Hard", "Very Hard" };

    static {
        initialize();
    }

    /**
     * Initialize the settings.
     * Will also reset the settings to loaded from database (or default) if called
     * later.
     */
    public static void initialize() {
        if (Database.isConnected()) {
            readFromDatabase();
            currentProfile = ProfileSettings.findProfileById(
                    profiles, SavedSettings.readCurrentProfile());
        } else {
            // can't read from database - create a default profile instead
            profiles = new ArrayList<ProfileSettings>();
            profiles.add(ProfileSettings.getDefaultProfile());
            deletedProfileIds = new ArrayList<Integer>();
            currentProfile = profiles.get(0);
        }
    }

    /**
     * @return current profile object
     */
    public static ProfileSettings getCurrentProfile() {
        return currentProfile;
    }

    /**
     * @return current profile name
     */
    public static String getCurrentProfileName() {
        return currentProfile.getName();
    }

    /**
     * Switch profile to the one with the given name.
     *
     * @param profileName name of the profile to switch to
     * @throws IllegalArgumentException when the given profile does not exist
     */
    public static void setCurrentProfile(String profileName) {
        ProfileSettings profile = ProfileSettings.findProfileByName(profiles, profileName);
        if (profile == null) {
            throw new IllegalArgumentException("Tried to set a nonexistent profile.");
        }
        currentProfile = profile;
    }

    /**
     * Create a new profile. The name must be different from all current profiles.
     * The new profile is a copy of the current profile.
     *
     * @param newProfileName name of the new profile
     * @throws RuntimeException when the given profile already exists
     */
    public static void addNewProfile(String newProfileName) {
        if (ProfileSettings.findProfileByName(profiles, newProfileName) != null) {
            throw new RuntimeException("Tried to add an existing profile.");
        }
        ProfileSettings profile = new ProfileSettings();
        profile.setId(-1);
        profile.setName(newProfileName);
        profile.setMusicVolume(getMusicVolume());
        profile.setSfxVolume(getSfxVolume());
        profile.setGameDifficulty(getGameDifficulty());
        profile.setGameLength(getGameLength());
        profile.setSharpshooterLength(getSharpshooterLength());
        profile.setKey1(getKeys().get(0));
        profile.setKey2(getKeys().get(1));
        profile.setKey3(getKeys().get(2));
        profile.setKey4(getKeys().get(3));
        profiles.add(profile);
    }

    /**
     * Delete the given profile. There must be another profile to switch to.
     *
     * @param profile name of the profile to be deleted
     * @throws IllegalArgumentException when the given profile does not exist
     * @throws RuntimeException when the given profile is the only one
     */
    public static void deleteProfile(String profileName) {
        ProfileSettings profile = ProfileSettings.findProfileByName(profiles, profileName);
        if (profile == null) {
            throw new IllegalArgumentException("Tried to delete a nonexistent profile.");
        }
        if (profiles.size() == 1) {
            throw new RuntimeException("Tried to delete the only profile.");
        }
        profiles.remove(profile);

        // if deleting current profile, switch to 0th profile
        if (currentProfile == profile) {
            // not using setCurrentProfile, no need to search the profile by name
            currentProfile = profiles.get(0);
        }
        if (profile.getId() != -1) {
            deletedProfileIds.add(profile.getId());
        }
    }

    /**
     * Get a copy of the list of names of all profiles.
     *
     * @return list of the profile names
     */
    public static List<String> getProfileNames() {
        List<String> profileNames = new ArrayList<String>();
        for (ProfileSettings profile : profiles) {
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
     *
     * @param newMusicVolume music volume to be set
     * @throws IllegalArgumentException when the given volume is out of range
     *                                  [0,100] (inclusive)
     */
    public static void setMusicVolume(Integer newMusicVolume) {
        if (newMusicVolume > 100 || newMusicVolume < 0) {
            throw new IllegalArgumentException("Music volume must be between 0 and 100 (inclusive)");
        }
        currentProfile.setMusicVolume(newMusicVolume);
    }

    /**
     * @return sound effects volume for the current profile
     */
    public static Integer getSfxVolume() {
        return currentProfile.getSfxVolume();
    }

    /**
     * Set sound effects volume for the current profile.
     *
     * @param newSfxVolume sound effects volume to be set
     * @throws IllegalArgumentException when the given volume is out of range
     *                                  [0,100] (inclusive)
     */
    public static void setSfxVolume(Integer newSfxVolume) {
        if (newSfxVolume > 100 || newSfxVolume < 0) {
            throw new IllegalArgumentException("Sfx volume must be between 0 and 100 (inclusive)");
        }
        currentProfile.setSfxVolume(newSfxVolume);
    }

    /**
     * @return game difficulty for the current profile
     */
    public static Integer getGameDifficulty() {
        return currentProfile.getGameDifficulty();
    }

    /**
     * Set game difficulty for the current profile.
     *
     * @param newGameDifficulty difficulty to be set
     * @throws IllegalArgumentException when the given number is out of range
     *                                  [0,4] (inclusive).
     */
    public static void setGameDifficulty(Integer newGameDifficulty) {
        if (newGameDifficulty < 0 || newGameDifficulty > 4) {
            throw new IllegalArgumentException("Invalid game difficulty");
        }
        currentProfile.setGameDifficulty(newGameDifficulty);
    }

    /**
     * @return single game length for the current profile in seconds
     */
    public static Integer getGameLength() {
        return currentProfile.getGameLength();
    }

    /**
     * Set single game length for the current profile.
     *
     * @param newGameLength single game length in seconds to be set
     * @throws IllegalArgumentException when the given game length is out of range
     *                                  [5,60] (inclusive)
     */
    public static void setGameLength(Integer newGameLength) {
        if (newGameLength > 60 || newGameLength < 5) {
            throw new IllegalArgumentException("Single game length must be between 5 and 60 (inclusive)");
        }
        currentProfile.setGameLength(newGameLength);
    }

    /**
     * @return single game length for the current profile in seconds
     */
    public static Integer getSharpshooterLength() {
        return currentProfile.getSharpshooterLength();
    }

    /**
     * Set sound effects volume for the current profile.
     *
     * @param newGameLength single game length in seconds to be set
     * @throws IllegalArgumentException when the given sharpshooter length is out of
     *                                  range [3,40] (inclusive)
     */
    public static void setSharpshooterLength(Integer newSharpshooterLength) {
        if (newSharpshooterLength > 40 || newSharpshooterLength < 3) {
            throw new IllegalArgumentException("Single game length must be between 3 and 40 (inclusive)");
        }
        currentProfile.setSharpshooterLength(newSharpshooterLength);
    }

    /**
     * @return a list of keys for KeyboardWarrior.
     */
    public static List<String> getKeys() {
        List<String> keys = new ArrayList<String>();
        keys.add(currentProfile.getKey1());
        keys.add(currentProfile.getKey2());
        keys.add(currentProfile.getKey3());
        keys.add(currentProfile.getKey4());
        return keys;
    }

    /**
     * Sets the key given as keyNumber (1, 2, 3 or 4) to the given key.
     *
     * @param keyNumber number of the key to set
     * @param value of the key to set
     * @throws IllegalArgumentException when key number is out of range
     */
    public static void setKey(int keyNumber, String key) {
        switch (keyNumber) {
            case 1:
                currentProfile.setKey1(key);
                break;
            case 2:
                currentProfile.setKey2(key);
                break;
            case 3:
                currentProfile.setKey3(key);
                break;
            case 4:
                currentProfile.setKey4(key);
                break;
            default:
                throw new IllegalArgumentException("Invalid key number");
        }
    }

    /**
     * Writes all changes to settings to the database (if connected).
     */
    public static void writeToDatabase() {
        if (Database.isConnected()) {
            SavedSettings.updateAllSettings(profiles, deletedProfileIds);
            deletedProfileIds = new ArrayList<Integer>();
            SavedSettings.writeCurrentProfile(currentProfile);
        }
    }

    /**
     * Reads all settings from the database, replacing the current ones
     * (if there are any).
     * If not connected to the database, does nothing.
     */
    public static void readFromDatabase() {
        if (Database.isConnected()) {
            profiles = SavedSettings.readAllSettings();
            deletedProfileIds = new ArrayList<Integer>();
            currentProfile = ProfileSettings.findProfileById(
                    profiles, SavedSettings.readCurrentProfile());
        }
    }

    /**
     * Writes the current profile to the database.
     */
    public static void saveCurrentProfile() {
        if (Database.isConnected()) {
            SavedSettings.writeCurrentProfile(currentProfile);
        }
    }
}