package app.DAO;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.service.ServiceRegistry;

import static org.hibernate.boot.registry.StandardServiceRegistryBuilder.destroy;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static Session session;
    private static ServiceRegistry registry;
    private static Transaction transact;

    public static void StartSessionFactory() {
        try {
            Configuration config = new Configuration().configure("/hibernate.cfg.xml");
            registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

            sessionFactory = config.buildSessionFactory(registry);
        } catch (HibernateException ex) {
            // Log the exception.
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void CloseConnection() {
        sessionFactory.close();
        destroy(registry);
    }

    public static void OpenConnection() {
        StartSessionFactory();
    }

    public void openSessionWithoutTrans() {
        session = getSessionFactory().openSession();
    }

    public void openSessionWithTrans() {
        try {
            session = getSessionFactory().openSession();
            transact = session.beginTransaction();
        } catch (Exception e) {
            HibernateUtil.CloseConnection();
            HibernateUtil.OpenConnection();
            session = getSessionFactory().openSession();
            transact = session.beginTransaction();
        }
    }

    public void closeSessionWithTrans() {
        if (!(transact.getStatus() == TransactionStatus.COMMITTED)) {
            transact.commit();
            getCurrentLocalSession().close();
        } else {
            if (getCurrentLocalSession().isOpen()) {
                getCurrentLocalSession().close();
            }

        }
    }

    public Session getCurrentLocalSession() {
        return session;
    }
}
