package com.skillbox.notification.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitQueueConfig {
    private String name;
    private String dlx;
    private String dlqRoutingKey;
    private Integer ttl;

    // More attributes can be added...
}
