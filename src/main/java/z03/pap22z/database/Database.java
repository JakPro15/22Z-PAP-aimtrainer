package z03.pap22z.database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class Database {
    static EntityManagerFactory ENTITY_MANAGER_FACTORY = null;

    /**
     * Connect to the MySQL database, if not already connected.
     */
    public static void connect() {
        if(ENTITY_MANAGER_FACTORY == null) {
            ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("PAP");
        }
    }

    /**
     * @return whether the database is successfully connected
     */
    public static boolean isConnected() {
        return ENTITY_MANAGER_FACTORY != null;
    }

    /**
     * Closes the connection to the MySQL database, if there is one.
     */
    public static void closeConnection() {
        if(ENTITY_MANAGER_FACTORY != null) {
            ENTITY_MANAGER_FACTORY.close();
            ENTITY_MANAGER_FACTORY = null;
        }
    }

    /**
     * Executes the given resource script in the database.
     *
     * @param filename script file name
     */
    public static void executeScript(String filename) {
        InputStream in = Database.class.getResourceAsStream(filename);
        Reader reader = new InputStreamReader(in);
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            int read = -1;
            String queryText = new String();
            while ((read = reader.read()) != -1) {
                char character = (char) read;
                // skip whitespace
                if (queryText.equals("") && " \r\n\t".indexOf(character) != -1) {
                    continue;
                }
                queryText = queryText + character;
                if (character == ';') {
                    manager.createNativeQuery(queryText).executeUpdate();
                    queryText = new String();
                }
            }
            transaction.commit();
        } catch (IOException e) {
            System.err.println("Failed to read from %s" + filename);
            System.err.println("Exception: %s" + e.getMessage());
            transaction.rollback();
        } finally {
            manager.close();
        }
    }

    /**
     * Resets the database to its default state.
     */
    public static void resetDatabase() {
        Database.executeScript("reset_database.sql");
    }
}
