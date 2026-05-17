package agroscience.notifications;

import agroscience.notifications.models.NotificationTemplate;
import agroscience.notifications.repositories.NotificationTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static agroscience.notifications.constants.Constants.NOTIFICATION_REQUESTS_TOPIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

@Slf4j
class NotificationsServiceApplicationTests extends BaseIntegrationTest {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private NotificationTemplateRepository templateRepository;

  @Autowired
  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  @MockitoBean
  private JavaMailSender mailSender;

  @BeforeEach
  void setUp() {
    templateRepository.deleteAll();
    templateRepository.save(NotificationTemplate.builder()
        .notificationType("EMAIL_CONFIRMATION")
        .channel("EMAIL")
        .subject("Confirm your registration")
        .body("Hello! Follow the link: http://{{user_id}}/confirm?trace={{trace_id}}")
        .build());
  }

  @Test
  void whenKafkaMessageReceived_thenEmailSent() {
    kafkaListenerEndpointRegistry.getListenerContainers()
        .forEach(container -> ContainerTestUtils.waitForAssignment(container, 1));

    String userId = UUID.randomUUID().toString();
    String traceId = UUID.randomUUID().toString();
    String email = "test@gmail.com";

    var future = kafkaTemplate.send(NOTIFICATION_REQUESTS_TOPIC, """
                {
                    "user_id": "%s",
                    "email": "%s",
                    "trace_id": "%s",
                    "notification_type": "EMAIL_CONFIRMATION"
                }
                """.formatted(userId, email, traceId));

    future.whenComplete((result, ex) -> {
      if (ex != null) {

      } else {
            result.getRecordMetadata().offset(),
            result.getRecordMetadata().partition());
      }
    });

    await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
      ArgumentCaptor<SimpleMailMessage> captor =
          ArgumentCaptor.forClass(SimpleMailMessage.class);
      verify(mailSender).send(captor.capture());
      SimpleMailMessage sent = captor.getValue();
      assertThat(sent.getTo()).contains(email);
      assertThat(sent.getSubject()).isEqualTo("Confirm your registration");
      assertThat(sent.getText()).contains(userId).contains(traceId);
    });
  }
}