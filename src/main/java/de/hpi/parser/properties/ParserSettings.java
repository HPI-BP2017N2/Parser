package de.hpi.parser.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("parser-settings")
@Getter
@Setter
public class ParserSettings {

    @NotBlank
    private String shopRulesGeneratorRoot;

    @NotBlank
    private String getRulesRoute;

}