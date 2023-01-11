package z03.pap22z.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

public class SavedStatistics {
    /**
     * Returns the calculated statistics of results.
     *
     * @return object mapping game type strings to their Statistics objects
     */
    public static Map<String, Statistics> readAllStatistics() {
        List<Statistics> resultList = null;
        Map<String, Statistics> resultMap = new HashMap<String, Statistics>();
        resultMap.put("AimSniper", null);
        resultMap.put("KeyboardWarrior", null);
        resultMap.put("Sharpshooter", null);
        EntityManager manager = Database.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            resultList = manager.createQuery("SELECT s FROM Statistics s", Statistics.class).getResultList();
            for(Statistics stat: resultList) {
                resultMap.put(stat.getGameType(), stat);
            }
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Exception in readAllStatistics: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
        return resultMap;
    }

    /**
     * Clears all saved StatResults and Results, and by extension, Statistics.
     */
    public static void clearAllStatisticsAndResults() {
        EntityManager manager = Database.ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            Query query = manager.createQuery("DELETE FROM StatResult s");
            query.executeUpdate();
            transaction.commit();
        }
        catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Exception in clearAllStatisticsAndResults: " + ex.getMessage());
        }
        finally {
            manager.close();
        }
    }
}
