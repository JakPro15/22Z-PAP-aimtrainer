package z03.pap22z;

public class Settings {
    public static String currentProfile;
    public static int musicVolume;

    static {
        System.out.println("Settings setup");
        // temporary values
        // in the end this will load data from the last active profile
        currentProfile = "default";
        musicVolume = 50;
    }
}
