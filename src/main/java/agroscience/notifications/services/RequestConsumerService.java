package agroscience.notifications.services;

import agroscience.notifications.clases.MailStructure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static agroscience.notifications.constants.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestConsumerService {

  private final MailService mailService;
  ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = NOTIFICATION_REQUESTS_TOPIC)
  public void listener(ConsumerRecord<String,String> consumerRecord ) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(consumerRecord.value());
    String email = jsonNode.get(EMAIL).asText();
    String userId = jsonNode.get(USER_ID).asText();
    String traceId = jsonNode.get(TRACE_ID).asText();

    mailService.sendMail(email, new MailStructure("confirm registration","To confirm your email, " +
            "follow the link: \n" + "link: http://" + userId + traceId +"."));
    log.info("Received message from topic 'agro.notifications.requests' with payload='{}'", consumerRecord.value());
  }

}
