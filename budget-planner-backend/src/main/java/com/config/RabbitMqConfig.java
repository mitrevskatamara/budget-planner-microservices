package com.config;

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

    @Value("${budgetplanner.queue.name}")
    private String budgetPlannerQueue;

    @Value("${budgetplanner.routingkey}")
    private String budgetPlannerRoutingKey;

    @Value("${notification.received.queue.name}")
    private String notificationReceivedQueue;

    @Value("${notification.received.routingkey}")
    private String notificationReceivedRoutingKey;

    @Bean
    TopicExchange budgetPlannerTopic() {
        return new TopicExchange(budgetPlannerExchange);
    }

    @Bean
    Queue budgetPlannerQueue() {
        return createQueue(budgetPlannerQueue);
    }

    @Bean
    Binding budgetPlannerBinding() {
        return createBinding(budgetPlannerQueue(), budgetPlannerRoutingKey, budgetPlannerTopic());
    }

    @Bean
    Queue notificationReceivedQueue() {
        return createQueue(notificationReceivedQueue);
    }

    @Bean
    Binding notificationReceivedBinding() {
        return createBinding(notificationReceivedQueue(), notificationReceivedRoutingKey, budgetPlannerTopic());
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
