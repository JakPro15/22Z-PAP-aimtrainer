package z03.pap22z.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;
import z03.pap22z.Settings;

import java.time.LocalDateTime;
import java.util.List;


@RunWith(JUnit4.class)
public class SavedResultsTest extends TestCase {
    @Before
    public void setup() throws Exception {
        Database.connect();
        Database.resetDatabase();
    }

    @After
    public void cleanup() {
        Database.resetDatabase();
    }

    @Test
    public void testWriteResult() {
        LocalDateTime now = LocalDateTime.now();
        StatResult statResult = SavedResults.writeStatResult(500, 0.5, "AimSniper");
        SavedResults.writeResult(statResult, now);
        // round now to full seconds
        if(now.getNano() >= 500_000_000) {
            now = now.plusSeconds(1);
        }
        now = now.withNano(0);
        List<Result> results = SavedResults.readAllResults();
        assertEquals(results.size(), 1);
        assertEquals(results.get(0).getScore(), 500);
        assertEquals(results.get(0).getAccuracy(), 0.5);
        assertEquals(results.get(0).getGameTime(), now);
        assertEquals(results.get(0).getGameType(), "AimSniper");
        assertEquals(results.get(0).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(0).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(0).getProfile().getId(), Settings.getCurrentProfile().getId());
    }

    @Test
    public void testReadAllResults() {
        LocalDateTime time1 = LocalDateTime.now();
        // round time1 to full seconds
        if(time1.getNano() >= 500_000_000) {
            time1 = time1.plusSeconds(1);
        }
        time1 = time1.withNano(0);
        LocalDateTime time2 = time1.plusSeconds(1);
        LocalDateTime time3 = time1.plusMinutes(1);
        StatResult statResult = SavedResults.writeStatResult(500, 0.5, "AimSniper");
        SavedResults.writeResult(statResult, time1);
        Settings.addNewProfile("new");
        Settings.addNewProfile("hehe xd");
        Settings.writeToDatabase();
        Settings.setCurrentProfile("new");
        statResult = SavedResults.writeStatResult(1500, 0.3, "KeyboardWarrior");
        SavedResults.writeResult(statResult, time2);
        Settings.setCurrentProfile("hehe xd");
        statResult = SavedResults.writeStatResult(1000, 0.6, "SpeedTyper");
        SavedResults.writeResult(statResult, time3);

        List<Result> results = SavedResults.readAllResults();
        assertEquals(results.size(), 3);
        assertEquals(results.get(0).getScore(), 1500);
        assertEquals(results.get(0).getAccuracy(), 0.3);
        assertEquals(results.get(0).getGameTime(), time2);
        assertEquals(results.get(0).getGameType(), "KeyboardWarrior");
        assertEquals(results.get(0).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(0).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(0).getProfile().getName(), "new");
        assertEquals(results.get(1).getScore(), 1000);
        assertEquals(results.get(1).getAccuracy(), 0.6);
        assertEquals(results.get(1).getGameTime(), time3);
        assertEquals(results.get(1).getGameType(), "SpeedTyper");
        assertEquals(results.get(1).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(1).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(1).getProfile().getName(), "hehe xd");
        assertEquals(results.get(2).getScore(), 500);
        assertEquals(results.get(2).getAccuracy(), 0.5);
        assertEquals(results.get(2).getGameTime(), time1);
        assertEquals(results.get(2).getGameType(), "AimSniper");
        assertEquals(results.get(2).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(2).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(2).getProfile().getName(), "default");
    }

    @Test
    public void testReadCurrentProfileResults() {
        LocalDateTime time1 = LocalDateTime.now();
        // round time1 to full seconds
        if(time1.getNano() >= 500_000_000) {
            time1 = time1.plusSeconds(1);
        }
        time1 = time1.withNano(0);
        LocalDateTime time2 = time1.plusSeconds(1);
        LocalDateTime time3 = time1.plusMinutes(1);
        StatResult statResult = SavedResults.writeStatResult(500, 0.5, "AimSniper");
        SavedResults.writeResult(statResult, time1);
        Settings.addNewProfile("new");
        Settings.writeToDatabase();
        Settings.setCurrentProfile("new");
        statResult = SavedResults.writeStatResult(1500, 0.3, "KeyboardWarrior");
        SavedResults.writeResult(statResult, time2);
        statResult = SavedResults.writeStatResult(1000, 0.6, "SpeedTyper");
        SavedResults.writeResult(statResult, time3);

        Settings.setCurrentProfile("default");
        List<Result> results = SavedResults.readCurrentProfileResults();
        assertEquals(results.size(), 1);
        assertEquals(results.get(0).getScore(), 500);
        assertEquals(results.get(0).getAccuracy(), 0.5);
        assertEquals(results.get(0).getGameTime(), time1);
        assertEquals(results.get(0).getGameType(), "AimSniper");
        assertEquals(results.get(0).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(0).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(0).getProfile().getName(), "default");

        Settings.setCurrentProfile("new");
        results = SavedResults.readCurrentProfileResults();
        assertEquals(results.size(), 2);
        assertEquals(results.get(0).getScore(), 1500);
        assertEquals(results.get(0).getAccuracy(), 0.3);
        assertEquals(results.get(0).getGameTime(), time2);
        assertEquals(results.get(0).getGameType(), "KeyboardWarrior");
        assertEquals(results.get(0).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(0).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(0).getProfile().getName(), "new");
        assertEquals(results.get(1).getScore(), 1000);
        assertEquals(results.get(1).getAccuracy(), 0.6);
        assertEquals(results.get(1).getGameTime(), time3);
        assertEquals(results.get(1).getGameType(), "SpeedTyper");
        assertEquals(results.get(1).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(1).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(1).getProfile().getName(), "new");
    }

    @Test
    public void testCascadeProfileDeletion() {
        LocalDateTime time1 = LocalDateTime.now();
        // round time1 to full seconds
        if(time1.getNano() >= 500_000_000) {
            time1 = time1.plusSeconds(1);
        }
        time1 = time1.withNano(0);
        LocalDateTime time2 = time1.plusSeconds(1);
        LocalDateTime time3 = time1.plusMinutes(1);
        StatResult statResult = SavedResults.writeStatResult(500, 0.5, "AimSniper");
        SavedResults.writeResult(statResult, time1);
        Settings.addNewProfile("new");
        Settings.addNewProfile("hehe xd");
        Settings.writeToDatabase();
        Settings.setCurrentProfile("new");
        statResult = SavedResults.writeStatResult(1500, 0.3, "KeyboardWarrior");
        SavedResults.writeResult(statResult, time2);
        Settings.setCurrentProfile("hehe xd");
        statResult = SavedResults.writeStatResult(1000, 0.6, "SpeedTyper");
        SavedResults.writeResult(statResult, time3);
        Settings.deleteProfile("new");
        Settings.writeToDatabase();

        List<Result> results = SavedResults.readAllResults();
        assertEquals(results.size(), 2);
        assertEquals(results.get(0).getScore(), 1000);
        assertEquals(results.get(0).getAccuracy(), 0.6);
        assertEquals(results.get(0).getGameTime(), time3);
        assertEquals(results.get(0).getGameType(), "SpeedTyper");
        assertEquals(results.get(0).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(0).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(0).getProfile().getName(), "hehe xd");
        assertEquals(results.get(1).getScore(), 500);
        assertEquals(results.get(1).getAccuracy(), 0.5);
        assertEquals(results.get(1).getGameTime(), time1);
        assertEquals(results.get(1).getGameType(), "AimSniper");
        assertEquals(results.get(1).getGameDifficulty(), (int) Settings.getGameDifficulty());
        assertEquals(results.get(1).getGameLength(), (int) Settings.getGameLength());
        assertEquals(results.get(1).getProfile().getName(), "default");
    }
}
