package org.fan.demo.hibernate;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.fan.demo.hibernate.entity.Student;
import org.fan.demo.hibernate.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.7.27 10:01
 */
@Slf4j
public class MainTest extends TestCase {

    private SessionFactory sessionFactory;

    @Override
    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void testBasicUsage() {
        // create a couple of events...
        Session session = null;
        /*Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save( new User("jack","peiKing") );
        session.save( new User( "rose", "hangZhou" ) );
        session.getTransaction().commit();
        session.close();*/

        // now lets pull events from the database and list them
        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Student where id=:id")
                .setParameter("id",1).list();
        for (Student user : (List<Student>) result) {
            log.info("user: {}", user);
        }
        session.getTransaction().commit();
        session.close();
    }
}
