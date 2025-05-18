package com.skillbox.notification.config;

import com.skillbox.notification.properties.RabbitBindingConfig;
import com.skillbox.notification.properties.RabbitQueueConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rabbit")
public class RabbitMqConfiguration {

    private List<String> exchanges = new ArrayList<>();
    private List<String> queues = new ArrayList<>();
    private List<RabbitBindingConfig> bindings = new ArrayList<>();

    @Bean
    public HashMap<String, Exchange> createExchanges(AmqpAdmin amqpAdmin) {
        HashMap<String, Exchange> exchangeMap = new HashMap<>();
        for (String exchangeName : exchanges) {
            Exchange exchange = ExchangeBuilder.directExchange(exchangeName).build();
            amqpAdmin.declareExchange(exchange);
            exchangeMap.put(exchangeName, exchange);
        }
        return exchangeMap;
    }

    @Bean
    public HashMap<String, Queue> createQueues(AmqpAdmin amqpAdmin) {
        HashMap<String, Queue> queueMap = new HashMap<>();
        for (String queueName : queues) {

            Queue q = QueueBuilder.durable(queueName).build();
            amqpAdmin.declareQueue(q);
            queueMap.put(queueName, q);
        }
        return queueMap;
    }

    @Bean
    public List<Binding> createBindings(AmqpAdmin amqpAdmin, HashMap<String, Queue> queueMap, HashMap<String, Exchange> exchangeMap) {
        List<Binding> bindingList = new ArrayList<>();
        for (RabbitBindingConfig bindingConfig : bindings) {
            Binding binding = BindingBuilder.bind(queueMap.get(bindingConfig.getQueue()))
                    .to(exchangeMap.get(bindingConfig.getExchange()))
                    .with(bindingConfig.getRoutingKey()).noargs();
            amqpAdmin.declareBinding(binding);
            bindingList.add(binding);
        }
        return bindingList;
    }
}
