package z03.pap22z.database;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import junit.framework.TestCase;

@RunWith(JUnit4.class)
public class DatabaseTest extends TestCase {
    @Before
    public void setup() {
        Database.connect();
    }

    @Test
    public void testConnection() {
        assertTrue(Database.isConnected());
    }

    @Test
    public void testResetConnection() {
        assertTrue(Database.isConnected());
        Database.closeConnection();
        assertFalse(Database.isConnected());
        Database.connect();
        assertTrue(Database.isConnected());
    }
}
