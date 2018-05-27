package de.hpi.parser.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@EnableRetry
@ConfigurationProperties("parser-settings")
@Getter
@Setter
public class ParserConfig {

    private String shopRulesGeneratorRoot;

    private String getRulesRoute;

}