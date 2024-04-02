package org.openexchangerates.config;

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

    @Value("${openexchangerates.queue.name}")
    private String openExchangeRatesQueue;

    @Value("${budgetplanner.queue.name}")
    private String budgetPlannerQueue;

    @Value("${openexchangerates.routingkey}")
    private String openExchangeRatesRoutingKey;

    @Value("${budgetplanner.routingkey}")
    private String budgetPlannerRoutingKey;

    @Bean
    Queue budgetPlannerQueue() {
        return createQueue(budgetPlannerQueue);
    }

    @Bean
    Binding budgetPlannerBinding() {
        return createBinding(budgetPlannerQueue(), budgetPlannerRoutingKey, budgetPlannerExchange());
    }

    @Bean
    TopicExchange deadLetterExchange() {
        return new TopicExchange(deadLetterExchange);
    }

    @Bean
    TopicExchange budgetPlannerExchange() {
        return new TopicExchange(budgetPlannerExchange);
    }

    @Bean
    Queue deadLetterQueue() {
        return createQueue(deadLetterQueue);
    }

    @Bean
    Queue openExchangeRatesQueue() {
        return createQueue(openExchangeRatesQueue);
    }

    @Bean
    Binding deadLetterBinding() {
        return createBinding(deadLetterQueue(), deadLetterQueue, deadLetterExchange());
    }

    @Bean
    Binding openExchangeRatesBinding() {
        return createBinding(openExchangeRatesQueue(), openExchangeRatesRoutingKey, budgetPlannerExchange());
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
