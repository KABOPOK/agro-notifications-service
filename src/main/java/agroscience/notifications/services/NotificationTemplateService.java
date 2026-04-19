package agroscience.notifications.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationTemplateService {
    public String resolve(String template, Map<String, String> payload) {
        for (Map.Entry<String, String> entry : payload.entrySet()) {
            template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return template;
    }
}
