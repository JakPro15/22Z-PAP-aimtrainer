package z03.pap22z.testfx;

import org.testfx.api.FxRobotException;
import org.testfx.framework.junit.ApplicationTest;

interface TestFXQueryFunction {
    void run(String query);
}

public class BaseTestFXTest extends ApplicationTest {
    /**
     * Try to click on a node, up to attempts times with the given delay.
     *
     * @param query query to find the node to click on
     * @param delayMillis delay in milliseconds
     * @param attempts max number of attempts
     * @return true if clicked, false if didn't
     */
    protected boolean tryToDo(TestFXQueryFunction runnable, String query, int delayMillis, int attempts) {
        while(attempts > 0) {
            try {
                runnable.run(query);
                return true;
            }
            catch(FxRobotException e) {}
            --attempts;
            sleep(delayMillis);
        }
        return false;
    }
}
