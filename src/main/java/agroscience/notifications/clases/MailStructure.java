package agroscience.notifications.clases;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailStructure {
  private String subject;
  private String message;
}
