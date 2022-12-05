package z03.pap22z.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import java.util.List;

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
        }
        ENTITY_MANAGER_FACTORY = null;
    }

    /**
     * Creates the default settings profile.
     */
    public static void createDefaultProfile() {
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            ProfileSettings defaultProfile = new ProfileSettings();
            defaultProfile.setId(0);
            defaultProfile.setName("default");
            defaultProfile.setMusicVolume(50);
            defaultProfile.setSfxVolume(50);
            defaultProfile.setGameSpeed(1.0);
            defaultProfile.setGameLength(20);
            manager.persist(defaultProfile);
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(ex.getMessage());
        }
        finally {
            manager.close();
        }
    }

    /**
     * Reads all profiles with their settings from the database.
     * @return list of the loaded profiles
     */
    public static List<ProfileSettings> readAllSettings() {
        List<ProfileSettings> result = null;
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            result = manager.createQuery("SELECT s FROM ProfileSettings s", ProfileSettings.class).getResultList();
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null)
                transaction.rollback();
            System.out.println(ex.getMessage());
        }
        finally {
            manager.close();
        }
        return result;
    }

    /**
     * Replaces the database settings list with the given one.
     * @param profiles list of profiles to be written to the database
     */
    public static void writeAllSettings(List<ProfileSettings> profiles) {
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            Query query = manager.createQuery("DELETE FROM ProfileSettings s");
            if(query.executeUpdate() == 0) {
                System.out.println("Failed to delete data from Settings table.");
                return;
            }
            for(ProfileSettings profile: profiles) {
                manager.persist(profile);
            }
            transaction.commit();
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        finally {
            manager.close();
        }
    }
}
