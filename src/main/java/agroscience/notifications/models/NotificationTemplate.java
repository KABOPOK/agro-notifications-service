package agroscience.notifications.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification_templates")
@Data
@Builder
public class NotificationTemplate {
    @Id
    private String id;
    private String notificationType;
    private String channel;
    private String subject;
    private String body;
}
