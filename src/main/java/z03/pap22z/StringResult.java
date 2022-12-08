package z03.pap22z;

import java.time.format.DateTimeFormatter;

import lombok.Data;
import z03.pap22z.database.Result;

@Data
public class StringResult {
    private int id;
    private String gameTime, user, gameType, speed, length, score, accuracy;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public StringResult(Result result) {
        id = result.getId();
        gameTime = result.getGameTime().format(dateFormatter);
        user = result.getProfile().getName();
        gameType = result.getGameType();
        speed = Settings.VALID_DIFFICULTIES[result.getGameDifficulty()];
        length = String.format("%ds", result.getGameLength());
        score = String.valueOf(result.getScore());
        accuracy = String.format("%.2f%%", result.getAccuracy());
    }
}