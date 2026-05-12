package homework.serviceuser.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final OutboxDispatchService outboxDispatchService;

    @Scheduled(fixedDelayString = "${outbox.publish-fixed-delay-ms:1000}")
    public void publishPending() {
        List<OutboxEntry> batch = outboxRepository.findTop50ByProcessedAtIsNullOrderByIdAsc();
        for (OutboxEntry entry : batch) {
            try {
                outboxDispatchService.dispatchById(entry.getId());
            } catch (Exception e) {
                log.warn("Outbox publish failed, id={}, will retry", entry.getId(), e);
            }
        }
    }
}
