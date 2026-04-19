package agroscience.notifications.services;

import agroscience.notifications.models.MailStructure;
import agroscience.notifications.models.NotificationTemplate;
import agroscience.notifications.repositories.NotificationTemplateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import static agroscience.notifications.constants.Constants.EMAIL;
import static agroscience.notifications.constants.Constants.NOTIFICATION_REQUESTS_TOPIC;
import static agroscience.notifications.constants.Constants.NOTIFICATION_TYPE;
import static agroscience.notifications.constants.Constants.TRACE_ID;
import static agroscience.notifications.constants.Constants.USER_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestConsumerService {

  private final MailService mailService;
  private final NotificationTemplateService templateService;
  private final NotificationTemplateRepository templateRepository;
  private final ObjectMapper objectMapper;


  @KafkaListener(topics = NOTIFICATION_REQUESTS_TOPIC)
  public void listener(ConsumerRecord<String, String> record) throws JsonProcessingException {
    JsonNode json = objectMapper.readTree(record.value());

    String email            = json.get(EMAIL).asText();
    String userId           = json.get(USER_ID).asText();
    String traceId          = json.get(TRACE_ID).asText();
    String notificationType = json.has(NOTIFICATION_TYPE)
        ? json.get(NOTIFICATION_TYPE).asText()
        : "EMAIL_CONFIRMATION";

    NotificationTemplate template = templateRepository
        .findByNotificationTypeAndChannel(notificationType, "EMAIL")
        .orElseThrow(() -> new IllegalStateException(
            "Template not found: type=%s, channel=EMAIL".formatted(notificationType)));

    Map<String, String> payload = Map.of(
        USER_ID,  userId,
        TRACE_ID, traceId,
        EMAIL,    email
    );

    String resolvedSubject = templateService.resolve(template.getSubject(), payload);
    String resolvedBody    = templateService.resolve(template.getBody(), payload);

    mailService.sendMail(email, new MailStructure(resolvedSubject, resolvedBody));

    log.info("Processed '{}' notification for email='{}', trace='{}'",
        notificationType, email, traceId);
  }

}
