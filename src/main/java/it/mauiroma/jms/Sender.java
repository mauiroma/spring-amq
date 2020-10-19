package it.mauiroma.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
public class Sender {

  private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
  private final String destination;

  @Autowired
  private JmsTemplate jmsTemplate;

  public Sender(String destination) {
    this.destination = destination;
  }

  public void send(String message) {
    LOGGER.info("sending message='{}'", message);
    jmsTemplate.convertAndSend(destination, message);
  }
}
