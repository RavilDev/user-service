package homework.serviceuser.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import homework.common.dto.UserMessageTo;
import homework.serviceuser.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxDispatchService {

    private final OutboxRepository outboxRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dispatchById(long outboxId) throws Exception {
        OutboxEntry entry = outboxRepository.findById(outboxId).orElseThrow();
        if (entry.getProcessedAt() != null) {
            return;
        }
        UserMessageTo message = objectMapper.readValue(entry.getPayload(), UserMessageTo.class);
        kafkaProducerService.sendSync(entry.getTopicName(), entry.getMessageKey(), message);
        entry.setProcessedAt(Instant.now());
        outboxRepository.save(entry);
    }
}
