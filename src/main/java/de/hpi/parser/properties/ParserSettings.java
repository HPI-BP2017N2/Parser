package de.hpi.parser.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("parser-settings")
@Getter
@Setter
public class ParserSettings {

    private String shopRulesGeneratorRoot;

    private String getRulesRoute;

}