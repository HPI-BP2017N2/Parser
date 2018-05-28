package de.hpi.parser.queue;

import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.properties.ParserConfig;
import de.hpi.parser.service.IParserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CrawledPagesConsumer {

    private final IParserService parserService;

    private final ParserConfig config;

    @RabbitListener(queues = "#{@crawledPages}")
    public void onMessage(CrawledPage crawledPage) throws InterruptedException {
        log.info("Received crawled page from shop " + crawledPage.getShopId() + " with url " + crawledPage.getUrl());
        try {
            getParserService().extractData(crawledPage);
        } catch (TaskRejectedException e) {
            log.info("Thread pool has no free capacity.", e);
            Thread.sleep(getConfig().getWaitIfThreadCapacityReachedInMilliseconds());
        }
    }
}
