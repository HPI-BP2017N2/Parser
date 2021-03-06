package de.hpi.parser.service;

import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.persistence.IParsedOfferRepository;
import de.hpi.parser.persistence.ParsedOffer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

import static de.hpi.parser.service.Normalizer.normalizeData;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
@Slf4j
public class ParserService implements IParserService {

    private final IParsedOfferRepository parsedOfferRepository;

    private final ShopRulesGenerator shopRulesGenerator;

    /**
     * Extract product specific attributes using the shop specific rules from the crawled pages.
     * @param crawledPage The webpage that we want to extract product attributes from.
     */
    @Override
    @Async("queueThreadPoolTaskExecutor")
    public void extractData(CrawledPage crawledPage) {
        try {
            ShopRules rules = getShopRulesGenerator().getRules(crawledPage.getShopId());
            Map<OfferAttribute, String> extractedData = extractData(rules.getSelectorMap(), Jsoup.parse(crawledPage
                    .getContent()));
            ParsedOffer parsedOffer = new ParsedOffer(extractedData, crawledPage);
            getParsedOfferRepository().save(parsedOffer);
        } catch (HttpClientErrorException e) {
            log.error("Could not get shop rules within retry time. Trying again...", e);
            extractData(crawledPage);
        }

    }

    private Map<OfferAttribute, String> extractData(EnumMap<OfferAttribute, Set<Selector>> selectorMap, Document page) {
        Map<OfferAttribute, String> extractedData = new EnumMap<>(OfferAttribute.class);
        selectorMap.forEach((offerAttribute, selectors) -> {
            if (OfferAttribute.EAN.equals(offerAttribute)) {
                Optional<String> ean = EANExtractor.extract(page);
                if (ean.isPresent()) {
                    extractedData.put(OfferAttribute.EAN, ean.get());
                    return;
                }
            }
            extractedData.put(offerAttribute, getBestMatchFor(selectors, page, offerAttribute));
        });
        return extractedData;
    }

    private String getBestMatchFor(Set<Selector> selectors, Document page, OfferAttribute offerAttribute) {
        HashMap<String, Double> scores = new HashMap<>();
        selectors.forEach(selector ->
                updateScoreMap(scores,
                        normalizeData(DataExtractor.extract(page, selector), offerAttribute),
                        selector.getNormalizedScore()));
        Optional<Map.Entry<String, Double>> optional = scores.entrySet().stream()
                .filter(entry -> entry.getKey().replace(" ", "").length() > 0)
                .max(Map.Entry.comparingByValue());
        return optional.isPresent() ? optional.get().getKey() : "";
    }

    private void updateScoreMap(HashMap<String, Double> scores, String content, double normalizedScore) {
        double prevScore = scores.getOrDefault(content, 0.0);
        scores.put(content, prevScore + normalizedScore);
    }

}
