package agroscience.notifications.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestProducerService {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  private static final String TOPIC = "agro.notification.requests";

  public void sendTestMessage() {
    String testMessage = String.format(
            "{ \"user_id\": \"%s\", \"email\": \"mishavvvsweater@gmail.com\", \"trace_id\": \"%s\" }",
            UUID.randomUUID(), UUID.randomUUID()
    );

    kafkaTemplate.send(TOPIC, testMessage);
    System.out.println("Test message sent: " + testMessage);
  }
}

