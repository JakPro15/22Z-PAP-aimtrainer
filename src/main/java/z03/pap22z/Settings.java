package z03.pap22z;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import z03.pap22z.database.Database;

import z03.pap22z.database.ProfileSettings;


public class Settings {
    private static Integer currentProfile;
    private static List<String> profileNames;

    private static List<Integer> musicVolumes;
    private static List<Integer> sfxVolumes;
    private static List<Double> gameSpeeds;
    private static List<Integer> gameLengths;

    public static final Double[] VALID_GAME_SPEEDS = {0.25, 0.5, 1.0, 2.0, 4.0};

    static {
        initialize();
    }

    /**
     * Initialize the settings.
     * Will also reset the settings to loaded from database or default if called later.
     */
    private static void initialize() {
        profileNames = new ArrayList<String>();
        musicVolumes = new ArrayList<Integer>();
        sfxVolumes = new ArrayList<Integer>();
        gameSpeeds = new ArrayList<Double>();
        gameLengths = new ArrayList<Integer>();

        if(Database.isConnected()) {
            List<ProfileSettings> profiles = Database.readAllSettings();
            if(profiles.size() == 0) {
                Database.createDefaultProfile();
                profiles = Database.readAllSettings();
            }
            setAllProfiles(profiles);
        }
        else {
            // can't read from database - create a default profile instead
            profileNames.add("default");
            musicVolumes.add(50);
            sfxVolumes.add(50);
            gameSpeeds.add(1.0);
            gameLengths.add(20);
        }
        currentProfile = 0;
    }

    /**
     * Set all profiles and settings to the given list of profiles with their settings.
     * Current profiles are replaced with the given ones.
     * @param profiles list of profiles to be set
     */
    public static void setAllProfiles(List<ProfileSettings> profiles) {
        profileNames = new ArrayList<String>();
        musicVolumes = new ArrayList<Integer>();
        sfxVolumes = new ArrayList<Integer>();
        gameSpeeds = new ArrayList<Double>();
        gameLengths = new ArrayList<Integer>();
        for(ProfileSettings profile: profiles) {
            profileNames.add(profile.getName());
            musicVolumes.add(profile.getMusicVolume());
            sfxVolumes.add(profile.getSfxVolume());
            gameSpeeds.add(profile.getGameSpeed());
            gameLengths.add(profile.getGameLength());
        }
        currentProfile = 0;
    }

    /**
     * @return current profile name
     */
    public static String getCurrentProfile() {
        return profileNames.get(currentProfile);
    }

    /**
     * Switch profile to the one with the given name.
     * @param profile name of the profile to switch to
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
     * @param newProfile name of the new profile
     * @throws RuntimeException when the given profile already exists
     */
    public static void addNewProfile(String newProfile) {
        if(profileNames.contains(newProfile)) {
            throw new RuntimeException("Tried to add an existing profile.");
        }
        profileNames.add(newProfile);
        musicVolumes.add(musicVolumes.get(currentProfile));
        sfxVolumes.add(sfxVolumes.get(currentProfile));
        gameSpeeds.add(gameSpeeds.get(currentProfile));
        gameLengths.add(gameLengths.get(currentProfile));
    }

    /**
     * Delete the given profile. There must be another profile to switch to.
     * @param profile name of the profile to be deleted
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
        sfxVolumes.remove(deletedIndex);
        gameSpeeds.remove(deletedIndex);
        gameLengths.remove(deletedIndex);

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
     * @param newMusicVolume music volume to be set
     * @throws IllegalArgumentException when the given volume is out of range [0,100] (inclusive)
     */
    public static void setMusicVolume(Integer newMusicVolume) {
        if(newMusicVolume > 100 || newMusicVolume < 0) {
            throw new IllegalArgumentException("Music volume must be between 0 and 100 (inclusive)");
        }
        musicVolumes.set(currentProfile, newMusicVolume);
    }

    /**
     * @return sound effects volume for the current profile
     */
    public static Integer getSfxVolume() {
        return sfxVolumes.get(currentProfile);
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
        sfxVolumes.set(currentProfile, newSfxVolume);
    }

    /**
     * @return game speed multiplier for the current profile
     */
    public static Double getGameSpeed() {
        return gameSpeeds.get(currentProfile);
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
        gameSpeeds.set(currentProfile, newGameSpeed);
    }

    /**
     * @return single game length for the current profile in seconds
     */
    public static Integer getGameLength() {
        return gameLengths.get(currentProfile);
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
        gameLengths.set(currentProfile, newGameLength);
    }

    /**
     * Writes all changes to settings to the database (if connected).
     */
    public static void writeToDatabase() {
        if(Database.isConnected()) {
            List<ProfileSettings> profiles = new ArrayList<ProfileSettings>();
            for(int i = 0; i < profileNames.size(); i++) {
                ProfileSettings profile = new ProfileSettings();
                profile.setId(i);
                profile.setName(profileNames.get(i));
                profile.setMusicVolume(musicVolumes.get(i));
                profile.setSfxVolume(sfxVolumes.get(i));
                profile.setGameSpeed(gameSpeeds.get(i));
                profile.setGameLength(gameLengths.get(i));
                profiles.add(profile);
            }
            Database.writeAllSettings(profiles);
        }
    }

    /**
     * Reads all settings from the database, replacing the current ones
     * (if there are any).
     * If not connected to the database, does nothing.
     */
    public static void readFromDatabase() {
        if(Database.isConnected()) {
            List<ProfileSettings> profiles = Database.readAllSettings();
            if(profiles.size() == 0) {
                Database.createDefaultProfile();
                profiles = Database.readAllSettings();
            }
            setAllProfiles(profiles);
        }
    }
}
