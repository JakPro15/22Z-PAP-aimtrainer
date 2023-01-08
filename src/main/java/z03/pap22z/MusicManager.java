package z03.pap22z;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicManager {
    private static MediaPlayer musicButton;
    private static MediaPlayer musicHitMarker;
    private static MediaPlayer musicMenuTheme;
    private static MediaPlayer musicFirstGameTheme;

    static {
        musicButton = MusicManager.createMediaPlayer("music/SimpleButton.mp3");
        musicHitMarker = MusicManager.createMediaPlayer("music/HitMarker.mp3");
        musicMenuTheme = MusicManager.createMediaPlayer("music/MenuTheme.mp3");
        musicFirstGameTheme = MusicManager.createMediaPlayer("music/FirstGameTheme.mp3");
    }

    protected static MediaPlayer createMediaPlayer(String filename) {
        String soundFile = MusicManager.class.getResource(filename).toExternalForm();
        Media media = new Media(soundFile);
        MediaPlayer player = new MediaPlayer(media);
        return player;
    }

    public static void setMusicVolume(float newVolume) {
        musicMenuTheme.setVolume(newVolume / 100.0f);
        musicFirstGameTheme.setVolume(newVolume / 100.0f);
    }

    public static void setSfxVolume(float newVolume) {
        musicButton.setVolume(newVolume / 100.0f);
        musicHitMarker.setVolume(newVolume / 100.0f);
    }

    public static void playButtonSound() {
        musicButton.seek(Duration.ZERO);
        musicButton.play();
    }

    public static void playHitMarkerSound() {
        musicHitMarker.seek(Duration.ZERO);
        musicHitMarker.play();
    }

    public static void playMenuTheme() {
        musicMenuTheme.seek(Duration.ZERO);
        musicMenuTheme.setCycleCount(MediaPlayer.INDEFINITE);
        musicMenuTheme.play();
    }

    public static void stopMenuTheme() {
        musicMenuTheme.stop();
    }

    public static void playFirstGameTheme() {
        musicFirstGameTheme.seek(Duration.ZERO);
        musicFirstGameTheme.setCycleCount(MediaPlayer.INDEFINITE);
        musicFirstGameTheme.play();
    }

    public static void stopFirstGameTheme() {
        musicFirstGameTheme.stop();
    }
}
