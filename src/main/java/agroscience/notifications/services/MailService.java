package agroscience.notifications.services;

import agroscience.notifications.clases.MailStructure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  @Autowired
  private ResponseProducerService responseProducerService;
  private final JavaMailSender mailSender;
  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }
  @Value("${spring.mail.username}")
  private String fromMail;
  public void sendMail(String mail, MailStructure mailStructure) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(fromMail);
    simpleMailMessage.setSubject(mailStructure.getSubject());
    simpleMailMessage.setText(mailStructure.getMessage());
    simpleMailMessage.setTo(mail);
    try {
      mailSender.send(simpleMailMessage);
      responseProducerService.sendResponse("Confirm-link was sent to " + mail);
    } catch (MailException e) {
      responseProducerService.sendResponse("failed: " + e.getMessage());
    }
  }

}

