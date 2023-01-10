package z03.pap22z.database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import z03.pap22z.Settings;

public class SavedResults {
    /**
     * Writes a statistical result and returns the StatResult written.
     * @param score score achieved in the game
     * @param accuracy accuracy achieved in the game
     * @param gameType type of the game
     * @return stat result created
     */
    public static StatResult writeStatResult(int score, double accuracy, String gameType) {
        EntityManager manager = Database.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            StatResult result = new StatResult();
            result.setScore(score);
            result.setAccuracy(accuracy);
            result.setGameType(gameType);
            manager.persist(result);
            transaction.commit();
            return result;
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Exception in writeStatResult: " + ex.getMessage());
            return null;
        }
        finally {
            manager.close();
        }
    }

    /**
     * Registers a result of a game in the database.
     * @param statResult previously written StatResult
     * @param gameTime time of the game
     */
    public static void writeResult(StatResult statResult, LocalDateTime gameTime) {
        EntityManager manager = Database.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            Result result = new Result();
            ProfileSettings currentProfile = manager.find(
                ProfileSettings.class, Settings.getCurrentProfile().getId()
            );
            result.setProfile(currentProfile);
            result.setGameTime(gameTime);
            result.setStatResult(statResult);
            result.setGameDifficulty(Settings.getGameDifficulty());
            if(statResult.getGameType().equals("Sharpshooter")) {
                result.setGameLength(Settings.getSharpshooterLength());
            }
            else {
                result.setGameLength(Settings.getGameLength());
            }
            manager.persist(result);
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Exception in writeResult: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
    }

    /**
     * @return a list of all the results from the database, ordered in descending order by score
     */
    public static List<Result> readAllResults() {
        List<Result> results = null;
        EntityManager manager = Database.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            results = manager.createQuery(
                "SELECT r FROM Result r INNER JOIN r.statResult ORDER BY Score DESC",
                Result.class
            ).getResultList();
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Exception in readAllSettings: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
        return results;
    }

    /**
     * @return a list of all the results from the database
     *         belonging to the current profile, ordered in descending order by score
     */
    public static List<Result> readCurrentProfileResults() {
        ProfileSettings currentProfile = null;
        EntityManager manager = Database.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            // update current profile to load new results
            currentProfile = manager.find(ProfileSettings.class, Settings.getCurrentProfile().getId());
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Exception in readCurrentProfileResults: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
        ArrayList<Result> results = new ArrayList<Result>(currentProfile.getResults());
        results.sort((first, second) -> {  // descending order
            if(first.getScore() < second.getScore()) {
                return 1;
            }
            else if(first.getScore() > second.getScore()) {
                return -1;
            }
            else {
                return 0;
            }
        });
        return results;
    }
}
