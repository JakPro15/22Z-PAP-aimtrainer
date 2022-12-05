package z03.pap22z.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
    public static void writeAllSettings(List<ProfileSettings> profiles, List<Integer> deletedIds) {
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            for(Integer id: deletedIds) {
                ProfileSettings profile = manager.find(ProfileSettings.class, id);
                if(profile != null) {
                    manager.remove(profile);
                }
            }
            for(ProfileSettings profile: profiles) {
                ProfileSettings savedProfile = manager.find(ProfileSettings.class, profile.getId());
                if(savedProfile == null) {
                    savedProfile = new ProfileSettings();
                }
                savedProfile.copyFromOther(profile);
                manager.persist(savedProfile);
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
