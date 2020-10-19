package it.mauiroma.jms;


import java.net.URISyntaxException;
import java.util.Enumeration;

import javax.jms.*;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;

public class Browser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Browser.class);
    private final String destination;

    @Autowired
    private JmsTemplate jmsTemplate;

    public Browser(String destination) {
        this.destination = destination;
    }


    public int browse() {
        LOGGER.info("Browsing Destination '{}'",destination);
        BrowserCallback browserCallback = (session, queueBrowser) -> {
            Enumeration e = queueBrowser.getEnumeration();
            int msgs = 0;
            while (e.hasMoreElements()) {
                TextMessage message = (TextMessage) e.nextElement();
                System.out.println("Browse [" + message.getText() + "]");
                msgs++;
            }
            LOGGER.info("Browse {} messages",msgs);
            return msgs;
        };
        return (int) jmsTemplate.browse(destination, browserCallback);
    }

    public void main(String[] args) throws URISyntaxException, Exception {
        Connection connection = null;
        try {
            // Producer
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    "(tcp://amq.mauiroma.it:61616,tcp://amq1.mauiroma.it:62616)?ha=true");
            connection = connectionFactory.createConnection("amq","password");
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("Queue");

            System.out.println("Browse through the elements in queue");
            QueueBrowser browser = session.createBrowser(queue);
            Enumeration e = browser.getEnumeration();
            while (e.hasMoreElements()) {
                TextMessage message = (TextMessage) e.nextElement();
                System.out.println("Browse [" + message.getText() + "]");
            }
            System.out.println("Done");
            browser.close();
            session.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
