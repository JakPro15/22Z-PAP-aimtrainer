package z03.pap22z.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RunWith(JUnit4.class)
public class SavedStatisticsTest extends TestCase {
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
    public void testReadAllStatistics() {
        Map<String, Statistics> stats = SavedStatistics.readAllStatistics();
        assertEquals(stats.size(), 3);
        Statistics stat = stats.get("AimSniper");
        assertNull(stats.get("AimSniper"));
        assertNull(stats.get("KeyboardWarrior"));
        assertNull(stats.get("Sharpshooter"));

        SavedResults.writeStatResult(100, 51.2, "AimSniper");
        SavedResults.writeStatResult(200, 60.0, "AimSniper");
        SavedResults.writeStatResult(144, 33.66, "Sharpshooter");

        List<Result> results = SavedResults.readAllResults();
        assertEquals(results.size(), 0);
        stats = SavedStatistics.readAllStatistics();
        assertEquals(stats.size(), 3);
        stat = stats.get("AimSniper");
        assertEquals(stat.getGameType(), "AimSniper");
        assertEquals(stat.getAverageScore(), 150.0);
        assertEquals(stat.getAverageAccuracy(), 55.6);
        stat = stats.get("KeyboardWarrior");
        assertNull(stat);
        stat = stats.get("Sharpshooter");
        assertEquals(stat.getGameType(), "Sharpshooter");
        assertEquals(stat.getAverageScore(), 144.0);
        assertEquals(stat.getAverageAccuracy(), 33.66);
    }

    @Test
    public void testClearAllStatisticsAndResults() {
        SavedResults.writeResult(SavedResults.writeStatResult(100, 51.2, "AimSniper"), LocalDateTime.now());
        SavedResults.writeStatResult(200, 60.0, "AimSniper");
        SavedResults.writeResult(SavedResults.writeStatResult(144, 33.66, "Sharpshooter"), LocalDateTime.now());

        SavedStatistics.clearAllStatisticsAndResults();

        List<Result> results = SavedResults.readAllResults();
        assertEquals(results.size(), 0);
        Map<String, Statistics> stats = SavedStatistics.readAllStatistics();
        assertEquals(stats.size(), 3);
        assertNull(stats.get("AimSniper"));
        assertNull(stats.get("KeyboardWarrior"));
        assertNull(stats.get("Sharpshooter"));
    }
}
