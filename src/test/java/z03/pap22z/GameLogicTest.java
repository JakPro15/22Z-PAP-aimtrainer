package z03.pap22z;

import org.junit.Test;

import junit.framework.TestCase;

public class GameLogicTest extends TestCase {
    @Test
    public void testConstructor() {
        GameLogic logic = new GameLogic();

        assertTrue(logic.pointsProperty().getValue().equals(0));

        assertTrue(logic.accuracyProperty().getValue().equals((double) 0));
    }

    @Test
    public void testGetters() {
        GameLogic logic = new GameLogic();

        assertTrue(logic.getPoints() == 0);

        assertTrue(logic.getAccuracy() == 0);

        assertFalse(logic.getIsGameOn());
    }

    @Test
    public void testChangeState() {
        GameLogic logic = new GameLogic();

        logic.toggleGameState();

        assertTrue(logic.getIsGameOn());

        logic.toggleGameState();

        assertFalse(logic.getIsGameOn());
    }

    @Test
    public void testRegisterHit() {
        GameLogic logic = new GameLogic();

        logic.registerTargetHit();

        assertTrue(logic.getPoints() == 10);

        assertTrue(logic.getAccuracy() == 100);

        logic.registerTargetHit();

        assertTrue(logic.getPoints() == 30);

        assertTrue(logic.getAccuracy() == 100);
    }

    @Test
    public void testRegisterMiss() {
        GameLogic logic = new GameLogic();

        logic.registerTargetMiss();

        assertTrue(logic.getPoints() == 0);

        assertTrue(logic.getAccuracy() == 0);
    }

    @Test
    public void testRegistersMixed() {
        GameLogic logic = new GameLogic();

        logic.registerTargetHit();
        logic.registerTargetMiss();

        assertTrue(logic.getPoints() == 10);
        assertTrue(logic.getAccuracy() == 50);

        logic.registerTargetHit();

        assertTrue(logic.getPoints() == 20);
        assertEquals(logic.getAccuracy(), (double) 200 / (double) 3, 1e-4);

        logic.registerTargetHit();

        assertTrue(logic.getPoints() == 40);
        assertEquals(logic.getAccuracy(), (double) 300 / (double) 4, 1e-4);

        logic.registerTargetMiss();

        assertTrue(logic.getPoints() == 40);
        assertEquals(logic.getAccuracy(), (double) 300 / (double) 5, 1e-4);
    }
}
