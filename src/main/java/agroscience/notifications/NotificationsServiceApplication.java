package agroscience.notifications;

import agroscience.notifications.services.TestProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class NotificationsServiceApplication {

  public static void main(String[] args) {
    ApplicationContext content = SpringApplication.run(NotificationsServiceApplication.class, args);
    content.getBean(TestProducerService.class).sendTestMessage();
  }

}
