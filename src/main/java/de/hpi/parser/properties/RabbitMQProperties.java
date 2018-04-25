package de.hpi.parser.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@Getter
@Primary
@RequiredArgsConstructor
public class RabbitMQProperties extends RabbitProperties {

    private final String queueName;

}