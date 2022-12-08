package utility;

import javax.persistence.EntityManager;

public final class JpaUtility {
    public static void flushAndClear(final EntityManager em) {
        em.flush();
        em.clear();
    }
}