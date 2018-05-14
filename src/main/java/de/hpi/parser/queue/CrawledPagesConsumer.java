package de.hpi.parser.queue;

import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.service.IParserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
        getParserService().extractData(crawledPage);
    }
}
