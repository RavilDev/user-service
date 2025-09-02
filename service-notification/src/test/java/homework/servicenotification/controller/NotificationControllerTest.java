package homework.servicenotification.controller;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import homework.common.dto.UserMessageTo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class NotificationControllerTest {

    @Container
    static final KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("apache/kafka:3.7.0")
    );

    @Container
    static final GenericContainer<?> greenMailContainer = new GenericContainer<>(
            DockerImageName.parse("greenmail/standalone:2.1.0")
    ).withExposedPorts(3025, 3110, 3143, 8080)
            .withEnv("GREENMAIL_OPTS", "-Dgreenmail.setup.test.all -Dgreenmail.users=test:test");

    private GreenMail greenMail;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> greenMailContainer.getMappedPort(3025));
        registry.add("spring.mail.username", () -> "test");
        registry.add("spring.mail.password", () -> "test");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "false");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() throws FolderException {
        ServerSetup smtpSetup = new ServerSetup(
                greenMailContainer.getMappedPort(3025), greenMailContainer.getHost(), "smtp"
        );
        greenMail = new GreenMail(new ServerSetup[]{smtpSetup});
        greenMail.setUser("test", "test");
        greenMail.purgeEmailFromAllMailboxes();
        greenMail.start();
    }

    @AfterEach
    void tearDown() {
        if (greenMail != null) {
            greenMail.stop();
        }
    }

    @Test
    void sendNotificationCreateOperationTest() throws MessagingException, IOException {
        UserMessageTo messageTo = UserMessageTo.builder()
                .operation("CREATE")
                .email("test@example.com")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserMessageTo> request = new HttpEntity<>(messageTo, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/notifications", request, String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email sent to test@example.com", response.getBody());

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        MimeMessage email = receivedMessages[0];
        assertEquals("test@example.com", email.getAllRecipients()[0].toString());
        assertEquals("Аккаунт создан!", email.getSubject());
        assertEquals("Здравствуйте! Ваш аккаунт на сайте был успешно создан.", email.getContent().toString().trim());
        assertEquals("test", email.getFrom()[0].toString());
    }

    @Test
    void sendNotificationDeleteOperationTest() throws Exception {
        UserMessageTo messageTo = UserMessageTo.builder()
                .operation("DELETE")
                .email("test@example.com")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserMessageTo> request = new HttpEntity<>(messageTo, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/notifications", request, String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email sent to test@example.com", response.getBody());

        boolean emailReceived = greenMail.waitForIncomingEmail(15000, 1);
        assertTrue(emailReceived, "Email was not received within 15 seconds");
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length, "Expected exactly one email, but got " + receivedMessages.length);
        MimeMessage email = receivedMessages[0];
        assertEquals("test@example.com", email.getAllRecipients()[0].toString());
        assertEquals("Аккаунт удалён", email.getSubject());
        assertEquals("Здравствуйте! Ваш аккаунт был удалён.", email.getContent().toString().trim());
        assertEquals("test", email.getFrom()[0].toString());
    }

    @Test
    void sendNotificationThrowsException() {
        UserMessageTo messageTo = UserMessageTo.builder()
                .operation("CREATE")
                .email("invalid-email")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserMessageTo> request = new HttpEntity<>(messageTo, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/notifications", request, String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(0, receivedMessages.length, "No email should be sent for invalid email, but got " + receivedMessages.length);
    }

}
