package agroscience.notifications.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Value("${agro.notification.responses}")
  private String topic;

  void sendResponse(String responsePayload) {
    kafkaTemplate.send(topic, responsePayload);
    log.info("Sent response to 'agro.notifications.responses' with payload='{}'", responsePayload);
  }

}
