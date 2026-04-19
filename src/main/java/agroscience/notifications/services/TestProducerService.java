package agroscience.notifications.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static agroscience.notifications.constants.Constants.MESSAGE_WAS_SENT;
import static agroscience.notifications.constants.Constants.NOTIFICATION_REQUESTS_TOPIC;

@Service
@RequiredArgsConstructor
public class TestProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendTestMessage() {
    String testMessage = String.format(
        "{ \"user_id\": \"%s\", \"email\": \"mishavvvsweater@gmail.com\", " +
            "\"trace_id\": \"%s\", \"notification_type\": \"EMAIL_CONFIRMATION\" }",
        UUID.randomUUID(), UUID.randomUUID()
    );

    kafkaTemplate.send(NOTIFICATION_REQUESTS_TOPIC, testMessage);
    System.out.println(MESSAGE_WAS_SENT + testMessage);
  }
}

