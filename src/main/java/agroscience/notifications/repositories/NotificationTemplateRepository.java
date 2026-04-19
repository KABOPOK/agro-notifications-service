package agroscience.notifications.repositories;

import agroscience.notifications.models.NotificationTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationTemplateRepository extends MongoRepository<NotificationTemplate, String> {
    Optional<NotificationTemplate> findByNotificationTypeAndChannel(String notificationType, String channel);
}
