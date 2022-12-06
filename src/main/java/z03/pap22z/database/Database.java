package z03.pap22z.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;
import java.sql.SQLException;

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
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in readAllSettings: " + ex.getMessage());
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
    public static void updateAllSettings(List<ProfileSettings> profiles, List<Integer> deletedIds) {
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
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in updateAllSettings: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
    }

    /**
     * Returns the ID of the current profile.
     * Returns ID, so that no copies of profiles are kept.
     * @return ID of current profile, -1 if failed to read
     */
    public static int readCurrentProfile() {
        int resultId = -1;
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            CurrentProfile result = manager.find(CurrentProfile.class, 1);
            if(result == null) {
                throw new SQLException("Failed to read from CurrentProfile table");
            }
            resultId = result.getProfile().getId();
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in readCurrentProfile: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
        return resultId;
    }

    public static void writeCurrentProfile(ProfileSettings profile) {
        EntityManager manager = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            // find persistent current profile object
            List<ProfileSettings> allProflies = manager.createQuery(
                "SELECT s FROM ProfileSettings s", ProfileSettings.class
            ).getResultList();
            profile = ProfileSettings.findProfileByName(allProflies, profile.getName());
            if(profile == null) {
                throw new SQLException("Did not find current profile in the Settings table");
            }
            // write it as current profile
            CurrentProfile currentProfileRecord = manager.find(CurrentProfile.class, 1);
            if(currentProfileRecord == null) {
                throw new SQLException("Failed to read from CurrentProfile table");
            }
            currentProfileRecord.setProfile(profile);
            manager.persist(currentProfileRecord);
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in writeCurrentProfile: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
    }
}
