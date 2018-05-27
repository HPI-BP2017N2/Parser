package de.hpi.parser.service;

import de.hpi.parser.dto.SuccessGetRulesResponse;
import de.hpi.parser.properties.ParserConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter(AccessLevel.PRIVATE)
@Component
@RequiredArgsConstructor
public class ShopRulesGenerator {

    private final ParserConfig config;

    private final RestTemplate restTemplate;

    @Retryable(
            value = { HttpClientErrorException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 300000))
    @Cacheable("rules")
    public ShopRules getRules(long shopID) {
        return getRestTemplate().getForObject(getRulesURI(shopID), SuccessGetRulesResponse.class).getData();
    }

    private URI getRulesURI(long shopID) {
        return UriComponentsBuilder.fromUriString(getConfig().getShopRulesGeneratorRoot())
                .path(getConfig().getGetRulesRoute() + shopID)
                .build()
                .encode()
                .toUri();
    }
}
