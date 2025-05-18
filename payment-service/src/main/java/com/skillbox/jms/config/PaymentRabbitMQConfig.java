package com.skillbox.jms.config;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import jakarta.jms.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class PaymentRabbitMQConfig {

    @Value("${app.rabbitmq.username}")
    private String username = "rabbit";
    @Value("${app.rabbitmq.password}")
    private String password = "rabbit";
    @Value("${app.rabbitmq.host}")
    private String host = "localhost";

    /**
     * Connection to broker
     */
    @Bean
    public ConnectionFactory jmsConnectionFactory() {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost(host);
        connectionFactory.setPort(5672);
        return connectionFactory;
    }

    /**
     * JmsListener configuration
     */
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all auto-configured defaults to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some settings if necessary.
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


    /**
     * Rabbit Serialization
     */
    @Bean
    public org.springframework.amqp.support.converter.MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}

