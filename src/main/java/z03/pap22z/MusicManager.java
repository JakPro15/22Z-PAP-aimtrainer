package z03.pap22z;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicManager {
    private static MediaPlayer musicCountdown;
    private static MediaPlayer musicGameOver;
    private static MediaPlayer musicMenuTheme;
    private static MediaPlayer musicFirstGameTheme;
    private static MediaPlayer musicSecondGameTheme;
    private static MediaPlayer musicThirdGameTheme;
    private static float buttonVolume;
    private static float hitmarkerVolume;
    private static float revolverVolume;

    static {
        musicCountdown = MusicManager.createMediaPlayer("music/Countdown.mp3");
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
        buttonVolume = newVolume / 100.0f;
        hitmarkerVolume = newVolume / 100.0f;
        revolverVolume = newVolume / 200.0f;
    }

    private static void playSound(MediaPlayer player, boolean indefinite) {
        player.seek(Duration.ZERO);
        if (indefinite) {
            player.setCycleCount(MediaPlayer.INDEFINITE);
        }
        player.play();
    }

    private static Runnable getMusicThread(String fileName, float volume, boolean indefinite) {
        return () -> {
            MediaPlayer player = MusicManager.createMediaPlayer(fileName);
            player.seek(Duration.ZERO);
            player.setVolume(volume);
            if (indefinite) {
                player.setCycleCount(MediaPlayer.INDEFINITE);
            }
            player.play();
        };
    }

    private static void playSFX(String fileName, float volume) {
        getMusicThread(fileName, volume, false).run();
    }

    public static void playButtonSound() {
        playSFX("music/SimpleButton.mp3", buttonVolume);
    }

    public static void playCountdownSound() {
        playSound(musicCountdown, false);
    }

    public static void playHitMarkerSound() {
        playSFX("music/HitMarker.mp3", hitmarkerVolume);
    }

    public static void playGameOverSound() {
        playSound(musicGameOver, false);
    }

    public static void playMenuTheme() {
        playSound(musicMenuTheme, true);
    }

    public static void stopMenuTheme() {
        musicMenuTheme.stop();
    }

    public static void playFirstGameTheme() {
        playSound(musicFirstGameTheme, true);
    }

    public static void playSecondGameTheme() {
        playSound(musicSecondGameTheme, true);
    }

    public static void playThirdGameTheme() {
        playSound(musicThirdGameTheme, true);
    }

    public static void playRevolverShot() {
        playSFX("music/RevolverShot.wav", revolverVolume);
    }

    public static void stopCountDownMusic() {
        musicCountdown.stop();
    }

    public static void stopAnyGameTheme() {
        musicFirstGameTheme.stop();
        musicSecondGameTheme.stop();
        musicThirdGameTheme.stop();
    }
}
