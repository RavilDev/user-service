package homework.servicenotification;

import homework.servicenotification.config.KafkaTopicProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceNotificationApplication.class, args);
    }

}
