package homework.servicenotification.kafka;

import homework.common.dto.UserMessageTo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @KafkaListener(topics = "${kafka.topic.user-events}")
    public void consume(UserMessageTo message) {
        String subject;
        String text;

        if ("CREATE".equals(message.getOperation())) {
            subject = "Аккаунт создан!";
            text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if ("DELETE".equals(message.getOperation())) {
            subject = "Аккаунт удалён";
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            return;
        }

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom(mailFrom);
        emailMessage.setTo(message.getEmail());
        emailMessage.setSubject(subject);
        emailMessage.setText(text);
        mailSender.send(emailMessage);
    }
}
