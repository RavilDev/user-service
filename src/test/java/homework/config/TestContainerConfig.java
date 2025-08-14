package homework.dao;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
public class TestContainerConfig {

    private SessionFactory sessionFactory;

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("user-service-test-dao")
                    .withDatabaseName("test_database")
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withExposedPorts(5432);

    static {
        postgreSQLContainer.start();

        Configuration cfg = new Configuration();
    }

}
