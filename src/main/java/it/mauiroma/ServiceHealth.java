package it.mauiroma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class ServiceHealth implements HealthIndicator {
    @Autowired
    private JmsTemplate jmsTemplate;

    private final String message_key = "Spring-AMQ";
    @Override
    public Health health() {
        if (!isRunning()) {
            return Health.down().withDetail(message_key, "Not Available").build();
        }
        return Health.up().withDetail(message_key, "Available").build();
    }

    private Boolean isRunning() {
        Boolean isRunning = true;
        try {
            jmsTemplate.convertAndSend("HealtQueue", "HEALT");
            Message message = jmsTemplate.receive("HealtQueue");
            return (((TextMessage)message).getText().equals("HEALT"));
        } catch (Exception e) {
            e.printStackTrace();
            isRunning = false;
        }
        return isRunning;
    }
}
