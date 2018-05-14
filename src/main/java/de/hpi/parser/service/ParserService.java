package de.hpi.parser.service;

import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.persistence.ParsedOffer;
import de.hpi.parser.persistence.ParsedOfferRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class ParserService implements IParserService {

    private final ParsedOfferRepository parsedOfferRepository;

    private final ShopRulesGenerator shopRulesGenerator;

    @Override
    public void extractData(CrawledPage crawledPage) {
        ShopRules rules = getShopRulesGenerator().getRules(crawledPage.getShopId());
        Map<OfferAttribute, String> extractedData = extractData(rules.getSelectors(), Jsoup.parse(crawledPage
                .getContent()));
        ParsedOffer parsedOffer = new ParsedOffer(extractedData, crawledPage);
        getParsedOfferRepository().save(parsedOffer);
    }

    private Map<OfferAttribute, String> extractData(EnumMap<OfferAttribute, Set<Selector>> selectorMap, Document page) {
        Map<OfferAttribute, String> extractedData = new EnumMap<>(OfferAttribute.class);
        selectorMap.forEach((offerAttribute, selectors) -> extractedData.put(offerAttribute, getBestMatchFor
                (selectors, page)));
        return extractedData;
    }

    private String getBestMatchFor(Set<Selector> selectors, Document page) {
        HashMap<String, Double> scores = new HashMap<>();
        selectors.forEach(selector ->
                updateScoreMap(scores, DataExtractor.extract(page, selector), selector.getNormalizedScore()));
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
