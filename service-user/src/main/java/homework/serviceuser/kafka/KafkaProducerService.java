package homework.serviceuser.kafka;

import homework.serviceuser.dto.notification.UserMessageTo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, UserMessageTo> kafkaTemplate;

    @Value("${kafka.topic.user-events}")
    private String topic;

    public void sendMessage(UserMessageTo userMessageTo) {
        kafkaTemplate.send(topic, userMessageTo.getEmail(), userMessageTo);
    }
}
