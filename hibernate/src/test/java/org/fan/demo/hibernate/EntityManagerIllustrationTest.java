package org.fan.demo.hibernate;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.fan.demo.hibernate.entity.User;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;

/**
 * Illustrates basic use of Hibernate as a JPA provider.
 *
 * @author Steve Ebersole
 */
@Slf4j
public class EntityManagerIllustrationTest extends TestCase {
    private EntityManagerFactory entityManagerFactory;

    @Override
    protected void setUp() throws Exception {
        // like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
        // 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
        entityManagerFactory = Persistence.createEntityManagerFactory( "org.hibernate.tutorial.jpa" );
    }

    @Override
    protected void tearDown() throws Exception {
        entityManagerFactory.close();
    }

    public void testBasicUsage() {
        // create a couple of events...
        EntityManager entityManager;

        // now lets pull events from the database and list them
        entityManager = entityManagerFactory.createEntityManager();
        CriteriaQuery<User> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(User.class);
        //criteriaQuery.select(criteriaQuery.s)
        criteriaQuery.select(criteriaQuery.from(User.class));
        entityManager.getTransaction().begin();
        List<User> result = entityManager.createQuery("from User", User.class).getResultList();
        for (User user : result) {
            log.info("user: {}", user);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
