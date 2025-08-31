package homework.servicenotification.controller;

import homework.common.dto.UserMessageTo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @PostMapping("/notifications")
    public String sendNotification(@RequestBody UserMessageTo messageTo) {
        String subject = "CREATE".equals(messageTo.getOperation()) ? "Добро пожаловать!" : "Аккаунт удалён";
        String text = "CREATE".equals(messageTo.getOperation()) ?
                "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан." :
                "Здравствуйте! Ваш аккаунт был удалён.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(messageTo.getEmail());
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
            return "Email sent to " + messageTo.getEmail();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}
