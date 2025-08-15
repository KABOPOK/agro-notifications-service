package agroscience.notifications.constants;

import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Constants {

    public static final String NOTIFICATION_REQUESTS_TOPIC = "agro.notification.requests";
    public static final String MESSAGE_WAS_SENT = "The message was sent : ";

    public static final String EMAIL =  "email";
    public static final String USER_ID = "user_id";
    public static final String TRACE_ID = "trace_id";

}
