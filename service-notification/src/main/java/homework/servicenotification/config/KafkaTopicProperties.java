package homework.servicenotification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicProperties {

    private String userEvents = "users";

    public String getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(String userEvents) {
        this.userEvents = userEvents;
    }

}
