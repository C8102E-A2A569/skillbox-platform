package com.skillbox.notification.adapter.rabbitmq.config;

import com.skillbox.notification.adapter.rabbitmq.service.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Если использовать spring-boot-starter-amqp то можно просто передать через application.properties параметры
 * spring.rabbitmq.host, spring.rabbitmq.port, ...
 * но так как adapter должен быть самостоятельным тут поднимаются бины
 * <p>
 * connectionFactory - для создания соединения;<br/>
 * rabbitTemplate - producer для отправки событий в очередь;<br/>
 * rabbitListenerContainerFactory - это для consumer (на самом деле не обязательно, тут его нет)<br/>
 */
@Configuration
@EnableConfigurationProperties({NotificationAdapterProperties.class})
@RequiredArgsConstructor
public class AdapterRabbitMQConfig {

    private final NotificationAdapterProperties properties;
    public String queueName = "notification-queue";

    @Bean
    public Queue notificationQueue() {
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(properties.getExchangeName());
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(properties.getRoutingKey());
    }

    /**
     * Connection factory, is needed for creating connection => configure connection here
     */
    @Bean("notificationRabbitConnectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(properties.getRabbitmq().getUsername());
        connectionFactory.setPassword(properties.getRabbitmq().getPassword());
        connectionFactory.setVirtualHost(properties.getRabbitmq().getVirtualHost());
        connectionFactory.setHost(properties.getRabbitmq().getHost());
        connectionFactory.setPort(properties.getRabbitmq().getPort());
        return connectionFactory;
    }

    /**
     * Rabbit Serialization
     */
    @Bean("jsonMessageConverter")
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * for Producer
     */
    @Bean("notificationRabbitTemplate")
    public RabbitTemplate rabbitTemplate(@Qualifier("notificationRabbitConnectionFactory") ConnectionFactory connectionFactory, @Qualifier("jsonMessageConverter") MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    /**
     * Consumer configuration (listener)
     *
     * @param connectionFactory class for connection configuration
     * @return SimpleRabbitListenerContainerFactory
     */
    @Bean("notificationListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            @Qualifier("notificationRabbitConnectionFactory") ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); // AUTO | MANUAL | NONE
        factory.setPrefetchCount(1);
        return factory;
    }

    @Bean
    public NotificationProducer notificationProducer(@Qualifier("notificationRabbitTemplate") RabbitTemplate rabbitTemplate, NotificationAdapterProperties properties) {
        return new NotificationProducer(rabbitTemplate, properties);
    }
}
