package de.hpi.parser.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MessageQueueConfig {

    @Bean
    public Queue priceValidatorQueue(SimpleRabbitListenerContainerFactory factory, MessageConverter messageConverter,
                                     RabbitMQProperties properties) {
        factory.setMessageConverter(messageConverter);
        return new Queue(properties.getQueueName(), true, false, false);
    }

    @Bean
    public MessageConverter messageConverter() {
        ContentTypeDelegatingMessageConverter messageConverter = new ContentTypeDelegatingMessageConverter();
        messageConverter.addDelegate("application/json",new Jackson2JsonMessageConverter());
        return messageConverter;
    }
}