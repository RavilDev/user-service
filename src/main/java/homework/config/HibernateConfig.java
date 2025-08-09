package homework.config;

import homework.entity.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class HibernateConfig {
    @Getter
    private static final SessionFactory sessionFactory =  buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {

            Properties properties = new Properties();
            properties.load(HibernateConfig.class.getResourceAsStream("/hibernate.properties"));

            Configuration configuration = new Configuration();
            configuration.addProperties(properties);
            configuration.addAnnotatedClass(User.class);
            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
