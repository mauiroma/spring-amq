package it.mauiroma;

import it.mauiroma.jms.Browser;
import it.mauiroma.jms.CustomErrorHandler;
import it.mauiroma.jms.Receiver;
import it.mauiroma.jms.Sender;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class ApplicationConfig {

    @Value("${artemis.broker-url}")
    private String brokerUrl;

    @Value("${artemis.broker-user}")
    private String brokerUser;

    @Value("${artemis.broker-password}")
    private String brokerPassword;

    @Value("${artemis.broker-consumer-concurrency}")
    private String brokerConsumerConcurrency;

    @Value("${artemis.destination}")
    private String destination;

    private ActiveMQConnectionFactory getActiveMQConnectionFactory() {
        return  new ActiveMQConnectionFactory(brokerUrl, brokerUser,brokerPassword);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(getActiveMQConnectionFactory());
        factory.setConcurrency(brokerConsumerConcurrency);
        factory.setErrorHandler(new CustomErrorHandler());
        factory.setSessionTransacted(true);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        CachingConnectionFactory ccf = new CachingConnectionFactory(getActiveMQConnectionFactory());
        return new JmsTemplate(ccf);
    }

    @Bean
    public Sender sender() {
        return new Sender(destination);
    }

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

    @Bean
    public Browser browser() {
        return new Browser(destination);
    }

}
