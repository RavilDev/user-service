package homework.serviceuser.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import homework.common.dto.UserMessageTo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventOutboxWriter {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.user-events}")
    private String userEventsTopic;

    public void enqueue(UserMessageTo message) {
        OutboxEntry entry = new OutboxEntry();
        entry.setTopicName(userEventsTopic);
        entry.setMessageKey(message.getEmail());
        try {
            entry.setPayload(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialize user event for outbox", e);
        }
        outboxRepository.save(entry);
    }
}
