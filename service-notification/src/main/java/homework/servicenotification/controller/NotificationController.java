package homework.servicenotification.controller;

import homework.common.dto.UserMessageTo;
import homework.servicenotification.kafka.KafkaConsumerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final KafkaConsumerService kafkaConsumerService;

    @PostMapping("/notifications")
    public String sendNotification(
            @Valid @RequestBody UserMessageTo messageTo) {
        try {
            kafkaConsumerService.sendEmail(messageTo);
            return "Email sent to " + messageTo.getEmail();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}
