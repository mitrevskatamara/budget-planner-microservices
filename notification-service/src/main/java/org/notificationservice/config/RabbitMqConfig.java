package org.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${deadletter.exchange}")
    private String deadLetterExchange;

    @Value("${budgetplanner.exchange}")
    private String budgetPlannerExchange;

    @Value("${deadletter.queue.name}")
    private String deadLetterQueue;

    @Value("${notification.service.queue.name}")
    private String notificationServiceQueue;

    @Value("${notification.service.routing.key}")
    private String notificationServiceRoutingKey;

    @Bean
    TopicExchange budgetPlannerTopic() {
        return new TopicExchange(budgetPlannerExchange);
    }

    @Bean
    Queue notificationServiceQueue() {
        return createQueue(notificationServiceQueue);
    }

    @Bean
    Binding notificationSericeBinding() {
        return createBinding(notificationServiceQueue(), notificationServiceRoutingKey, budgetPlannerTopic());
    }

    private Queue createQueue(String queueName) {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", deadLetterExchange)
                .withArgument("x-dead-letter-routing-key", deadLetterQueue)
                .build();
    }

    private Binding createBinding(Queue queue, String key, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(key);
    }
}
