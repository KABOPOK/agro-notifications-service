package agroscience.notifications.migrations;

import agroscience.notifications.models.NotificationTemplate;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "V001_AddEmailConfirmationTemplate", order = "001", author = "agroscience")
public class V001_AddEmailConfirmationTemplate {

    @Execution
    public void execute(MongoTemplate mongoTemplate) {
        NotificationTemplate template = NotificationTemplate.builder()
            .notificationType("EMAIL_CONFIRMATION")
            .channel("EMAIL")
            .subject("Confirm your registration")
            .body("Hello!\n\nTo confirm your email, follow the link:\nhttp://{{user_id}}/confirm?trace={{trace_id}}")
            .build();

        mongoTemplate.save(template, "notification_templates");
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.remove(
            org.springframework.data.mongodb.core.query.Query.query(
                org.springframework.data.mongodb.core.query.Criteria
                    .where("notificationType").is("EMAIL_CONFIRMATION")
                    .and("channel").is("EMAIL")
            ),
            "notification_templates"
        );
    }
}
