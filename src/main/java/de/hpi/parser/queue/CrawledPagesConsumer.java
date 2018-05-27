package de.hpi.parser.queue;

import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.persistence.ParsedOffer;
import de.hpi.parser.service.IParserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@Slf4j
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CrawledPagesConsumer {

    private final IParserService parserService;

    @RabbitListener(queues = "#{@crawledPages}")
    public void onMessage(CrawledPage crawledPage) {
        log.info("Received crawled page from shop " + crawledPage.getShopId() + " with url " + crawledPage.getUrl());
        CompletableFuture<ParsedOffer> parsedOffer = getParserService().extractData(crawledPage);
        try {
            parsedOffer.join();
        } catch (CompletionException e) {
            log.error("Could not get shop rules within retry time. Trying again...", e);
            onMessage(crawledPage);
        }
    }
}
