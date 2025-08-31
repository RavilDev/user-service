package homework.servicenotification.kafka;

import homework.servicenotification.dto.UserMessageTo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final JavaMailSender mailSender;

    @KafkaListener(topics = "${kafka.topic.user-events}")
    public void consume(UserMessageTo message) {
        String subject;
        String text;

        if ("CREATE".equals(message.getOperation())) {
            subject = "Добро пожаловать !";
            text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if ("DELETE".equals(message.getOperation())) {
            subject = "Аккаунт удалён";
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            return;
        }

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(message.getEmail());
        emailMessage.setSubject(subject);
        emailMessage.setText(text);
        mailSender.send(emailMessage);
    }
}
