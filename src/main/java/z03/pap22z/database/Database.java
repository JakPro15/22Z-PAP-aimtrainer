package z03.pap22z.database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Database {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = null;

    /**
     * Connect to the MySQL database, if not already connected.
     */
    public static void connect() {
        if(ENTITY_MANAGER_FACTORY == null) {
            ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("PAP");
        }
    }

    /**
     * Closes the connection to the MySQL database, if there is one.
     */
    public static void closeConnection() {
        if(ENTITY_MANAGER_FACTORY != null) {
            ENTITY_MANAGER_FACTORY.close();
        }
        ENTITY_MANAGER_FACTORY = null;
    }
}
