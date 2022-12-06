package z03.pap22z.database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import z03.pap22z.Settings;

public class SavedResults {
    /**
     * Registers a result of the game in the database.
     * @param score score achieved in the game
     * @param accuracy accuracy achieved in the game
     */
    public static void writeResult(int score, double accuracy, LocalDateTime gameTime, String gameType) {
        Result result = new Result();
        result.setProfile(Settings.getCurrentProfile());
        result.setScore(score);
        result.setAccuracy(accuracy);
        result.setGameTime(gameTime);
        result.setGameType(gameType);
        result.setGameSpeed(Settings.getGameSpeed());
        result.setGameLength(Settings.getGameLength());

        EntityManager manager = Database.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(result);
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in registerResult: " + ex.getMessage());
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
                "SELECT r FROM Result r ORDER BY Score DESC", Result.class
            ).getResultList();
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
        return results;
    }

    /**
     * @return a list of all the results from the database
     *         belonging to the current profile
     */
    public static List<Result> readCurrentProfileResults() {
        ArrayList<Result> results = new ArrayList<Result>(Settings.getCurrentProfile().getResults());
        results.sort((first, second) -> {
            if(first.getScore() < second.getScore()) {
                return -1;
            }
            else if(first.getScore() > second.getScore()) {
                return 1;
            }
            else {
                return 0;
            }
        });
        return results;
    }

    /**
     * Deletes a result from the database.
     * @param result the result to be deleted
     */
    public static void deleteResult(Result result) {
        EntityManager manager = Database.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            Result persistentResult = manager.find(Result.class, result.getId());
            if(persistentResult != null) {
                manager.remove(persistentResult);
            }
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception in deleteResult: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
    }

    /**
     * Deletes the given results from the database.
     * @param results list of the results to be deleted
     */
    public static void deleteResults(List<Result> results) {
        for(Result result: results) {
            deleteResult(result);
        }
    }
}
