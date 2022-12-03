package z03.pap22z.database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Results {
    public static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("PAP");
}
