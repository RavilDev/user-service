package java.homework.servicenotification.config;

import com.icegreen.greenmail.util.GreenMail;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@TestConfiguration
public class TestMailConfig {
    private static GreenMail greenMail;

    @BeforeAll
    static void setup() {
        greenMail = new GreenMail();
        greenMail.start();
    }

    @AfterAll
    static void tearDown() {
        greenMail.stop();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setPort(greenMail.getSmtp().getServerSetup().getPort());
        return mailSender;
    }

    @Bean
    public GreenMail greenMail() {
        return greenMail;
    }
}
