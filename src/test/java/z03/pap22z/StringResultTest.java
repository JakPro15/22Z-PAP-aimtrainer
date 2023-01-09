package z03.pap22z;

import java.time.LocalDateTime;

import org.junit.Test;

import junit.framework.TestCase;
import z03.pap22z.database.ProfileSettings;
import z03.pap22z.database.Result;
import z03.pap22z.database.StatResult;

public class StringResultTest extends TestCase {
    /**
     * Get a dummy result.
     *
     * @param profileName name of the profile to have this result assigned
     * @param time time to write to the result
     * @return the created result
     */
    private Result getResult(String profileName, LocalDateTime time) {
        Result result = new Result();
        result.setId(12);
        result.setStatResult(new StatResult());

        ProfileSettings profile = new ProfileSettings();
        profile.setName(profileName);
        result.setProfile(profile);

        result.setScore(200);
        result.setAccuracy(23.45);

        if (time == null) {
            time = LocalDateTime.now();
        }
        result.setGameTime(time);
        result.setGameType("AimSnip");
        result.setGameDifficulty(1);
        result.setGameLength(15);
        return result;
    }

    @Test
    public void testId() {
        Result result = getResult(null, null);
        StringResult stringized = new StringResult(result);
        assertEquals(stringized.getId(), 12);

        result.setId(234);
        stringized = new StringResult(result);
        assertEquals(stringized.getId(), 234);
    }

    @Test
    public void testGameTime() {
        LocalDateTime time = LocalDateTime.of(2022, 12, 10, 16, 0, 24);
        Result result = getResult(null, time);
        StringResult stringized = new StringResult(result);
        assertEquals(stringized.getGameTime(), "10-12-2022 16:00:24");

        time = LocalDateTime.of(2024, 10, 1, 12, 2, 0);
        result.setGameTime(time);
        stringized = new StringResult(result);
        assertEquals(stringized.getGameTime(), "01-10-2024 12:02:00");
    }

    @Test
    public void testUser() {
        Result result = getResult("abcd", null);
        StringResult stringized = new StringResult(result);
        assertEquals(stringized.getUser(), "abcd");

        result.getProfile().setName("eee e");
        stringized = new StringResult(result);
        assertEquals(stringized.getUser(), "eee e");
    }

    @Test
    public void testGameType() {
        Result result = getResult(null, null);
        StringResult stringized = new StringResult(result);
        assertEquals(stringized.getGameType(), "AimSnip");

        result.setGameType("ABcD");
        stringized = new StringResult(result);
        assertEquals(stringized.getGameType(), "ABcD");
    }

    @Test
    public void testDifficulty() {
        Result result = getResult(null, null);
        StringResult stringized = new StringResult(result);
        assertEquals(stringized.getDifficulty(), Settings.DIFFICULTIES[1]);

        result.setGameDifficulty(4);
        stringized = new StringResult(result);
        assertEquals(stringized.getDifficulty(), Settings.DIFFICULTIES[4]);
    }

    @Test
    public void testLength() {
        Result result = getResult(null, null);
        StringResult stringized = new StringResult(result);
        assertEquals(stringized.getLength(), "15s");

        result.setGameLength(40);
        stringized = new StringResult(result);
        assertEquals(stringized.getLength(), "40s");
    }

    @Test
    public void testScore() {
        Result result = getResult(null, null);
        StringResult stringized = new StringResult(result);
        assertEquals(stringized.getScore(), "200");

        result.setScore(40);
        stringized = new StringResult(result);
        assertEquals(stringized.getScore(), "40");
    }

    @Test
    public void testAccuracy() {
        Result result = getResult(null, null);
        StringResult stringized = new StringResult(result);
        assertEquals(stringized.getAccuracy(), "23.45%");

        result.setAccuracy(40);
        stringized = new StringResult(result);
        assertEquals(stringized.getAccuracy(), "40.00%");

        result.setAccuracy(87.567);
        stringized = new StringResult(result);
        assertEquals(stringized.getAccuracy(), "87.57%");
    }
}
