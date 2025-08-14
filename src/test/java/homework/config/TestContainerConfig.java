package homework.config;


import homework.entity.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Properties;

@Testcontainers
public class TestContainerConfig {

    @Getter
    private final static SessionFactory sessionFactory;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        try {
            Properties properties = new Properties();
            properties.load(HibernateConfig.class.getResourceAsStream("/hibernate-test.properties"));

            postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName(properties.getProperty("testcontainers.databaseName"))
                    .withUsername(properties.getProperty("hibernate.connection.username"))
                    .withPassword(properties.getProperty("hibernate.connection.password"))
                    .withExposedPorts(5432);
            postgreSQLContainer.start();

            Configuration configuration = new Configuration();
            configuration.addProperties(properties);

            configuration.setProperty("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void stop() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        postgreSQLContainer.stop();
    }
}
