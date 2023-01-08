package z03.pap22z;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicManager {
    private static MediaPlayer musicButton;
    private static MediaPlayer musicCountdown;
    private static MediaPlayer musicHitMarker;
    private static MediaPlayer musicGameOver;
    private static MediaPlayer musicMenuTheme;
    private static MediaPlayer musicFirstGameTheme;
    private static MediaPlayer musicSecondGameTheme;
    private static MediaPlayer musicThirdGameTheme;

    static {
        musicButton = MusicManager.createMediaPlayer("music/SimpleButton.mp3");
        musicCountdown = MusicManager.createMediaPlayer("music/Countdown.mp3");
        musicHitMarker = MusicManager.createMediaPlayer("music/HitMarker.mp3");
        musicGameOver = MusicManager.createMediaPlayer("music/GameOver.mp3");
        musicMenuTheme = MusicManager.createMediaPlayer("music/MenuTheme.mp3");
        musicFirstGameTheme = MusicManager.createMediaPlayer("music/FirstGameTheme.mp3");
        musicSecondGameTheme = MusicManager.createMediaPlayer("music/SecondGameTheme.mp3");
        musicThirdGameTheme = MusicManager.createMediaPlayer("music/ThirdGameTheme.mp3");
    }

    protected static MediaPlayer createMediaPlayer(String filename) {
        String soundFile = MusicManager.class.getResource(filename).toExternalForm();
        Media media = new Media(soundFile);
        MediaPlayer player = new MediaPlayer(media);
        return player;
    }

    public static void setMusicVolume(float newVolume) {
        musicCountdown.setVolume(newVolume / 300.0f);
        musicGameOver.setVolume(newVolume / 25.0f);
        musicMenuTheme.setVolume(newVolume / 300.0f);
        musicFirstGameTheme.setVolume(newVolume / 100.0f);
        musicSecondGameTheme.setVolume(newVolume / 100.0f);
        musicThirdGameTheme.setVolume(newVolume / 100.0f);
    }

    public static void setSfxVolume(float newVolume) {
        musicButton.setVolume(newVolume / 100.0f);
        musicHitMarker.setVolume(newVolume / 100.0f);
    }

    public static void playButtonSound() {
        musicButton.seek(Duration.ZERO);
        musicButton.play();
    }

    public static void playCountdownSound() {
        musicCountdown.seek(Duration.ZERO);
        musicCountdown.play();
    }

    public static void playHitMarkerSound() {
        musicHitMarker.seek(Duration.ZERO);
        musicHitMarker.play();
    }

    public static void playGameOverSound() {
        musicGameOver.seek(Duration.ZERO);
        musicGameOver.play();
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

    public static void playSecondGameTheme() {
        musicSecondGameTheme.seek(Duration.ZERO);
        musicSecondGameTheme.setCycleCount(MediaPlayer.INDEFINITE);
        musicSecondGameTheme.play();
    }

    public static void playThirdGameTheme() {
        musicThirdGameTheme.seek(Duration.ZERO);
        musicThirdGameTheme.setCycleCount(MediaPlayer.INDEFINITE);
        musicThirdGameTheme.play();
    }

    public static void stopAnyGameTheme() {
        musicFirstGameTheme.stop();
        musicSecondGameTheme.stop();
        musicThirdGameTheme.stop();
    }
}
