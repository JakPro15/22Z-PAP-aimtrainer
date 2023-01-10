package z03.pap22z;

import java.time.format.DateTimeFormatter;

import lombok.Data;
import z03.pap22z.database.Result;

@Data
public class StringResult {
    private int id;
    private String gameTime, user, gameType, difficulty, length, score, accuracy;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Creates a StringResult containing all fields of the given result converted to strings.
     * Only id is not converted, but saved as an integer.
     * @param result
     */
    public StringResult(Result result) {
        id = result.getId();
        gameTime = result.getGameTime().format(dateFormatter);
        user = result.getProfile().getName();
        gameType = result.getGameType();
        difficulty = Settings.DIFFICULTIES[result.getGameDifficulty()];
        if(gameType.equals("Sharpshooter")) {
            length = String.format("%d attempts", result.getGameLength());
        }
        else {
            length = String.format("%d seconds", result.getGameLength());
        }
        score = String.valueOf(result.getScore());
        accuracy = String.format("%.2f%%", result.getAccuracy());
    }
}