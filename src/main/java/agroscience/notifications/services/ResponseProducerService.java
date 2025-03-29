package agroscience.notifications.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResponseProducerService {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;
  @Value("${agro.notification.responses}")
  private String topic;
  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseProducerService.class);
  void sendResponse(String responsePayload) {
    kafkaTemplate.send(topic, responsePayload);
    LOGGER.info("Sent response to 'agro.notifications.responses' with payload='{}'", responsePayload);
  }

}
