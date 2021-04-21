package it.mauiroma.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;

import java.util.concurrent.TimeUnit;

@EnableJms
public class Receiver {


  private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

  @JmsListener(destination = "${artemis.destination}")
  public void receive(String message) {
    LOGGER.info("received message='{}'", message);
    if (message.equalsIgnoreCase("error")){
      LOGGER.info("going in error");
      throw new RuntimeException("Managed Error");
    }
    if (message.equalsIgnoreCase("sleep")){
      try {
        LOGGER.debug("Sleep for 30 seconds");
        TimeUnit.SECONDS.sleep(30);
        LOGGER.debug("Resume");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
