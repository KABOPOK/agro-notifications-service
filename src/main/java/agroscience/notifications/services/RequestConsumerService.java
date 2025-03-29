package agroscience.notifications.services;

import agroscience.notifications.clases.MailStructure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RequestConsumerService {
  @Autowired
  private MailService mailService;
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestConsumerService.class);
  @KafkaListener(topics = "${agro.notification.requests}", groupId = "notificationServiceGroup")
  public void listener(@Payload String requestPayload,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Long partitionId,
                       @Header(KafkaHeaders.OFFSET) Long offset) throws JsonProcessingException {
    LOGGER.info("Received message from topic 'agro.notifications.requests' " +
            "partition={} offset={} payload='{}'", partitionId, offset, requestPayload);
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(requestPayload);
    String email = jsonNode.get("email").asText();
    String userId = jsonNode.get("user_id").asText();
    String traceId = jsonNode.get("trace_id").asText();
    mailService.sendMail(email, new MailStructure("confirm registration","To confirm your email, " +
            "follow the link: \n" + "link: http://" + userId + traceId +"."));
  }

}
