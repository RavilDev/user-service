package homework.serviceuser.kafka;

import homework.common.dto.UserMessageTo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, UserMessageTo> kafkaTemplate;

    @Value("${kafka.topic.user-events}")
    private String topic;

    public void sendMessage(UserMessageTo userMessageTo) {
        kafkaTemplate.send(topic, userMessageTo.getEmail(), userMessageTo);
    }

    public void sendSync(String topicName, String key, UserMessageTo userMessageTo) throws Exception {
        SendResult<String, UserMessageTo> result = kafkaTemplate
                .send(topicName, key, userMessageTo)
                .get(10, TimeUnit.SECONDS);
        if (result.getRecordMetadata() == null) {
            throw new IllegalStateException("Kafka send completed without metadata");
        }
    }
}
