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

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class ParserService implements IParserService {

    private final ParsedOfferRepository parsedOfferRepository;

    private final ShopRulesGenerator shopRulesGenerator;

    @Override
    public void extractData(CrawledPage crawledPage) {
        ShopRules rules = getShopRulesGenerator().getRules(crawledPage.getShopID());
        Map<OfferAttribute, String> extractedData = extractData(rules.getSelectors(), Jsoup.parse(crawledPage
                .getContent()));
        ParsedOffer parsedOffer = new ParsedOffer(extractedData, crawledPage);
        getParsedOfferRepository().save(parsedOffer);
    }

    private Map<OfferAttribute, String> extractData(EnumMap<OfferAttribute, Set<Selector>> selectorMap, Document
            page) {
        Map<OfferAttribute, String> extractedData = new EnumMap<>(OfferAttribute.class);
        selectorMap.forEach((offerAttribute, selectors) -> extractedData.put(offerAttribute, selectors.isEmpty() ? null : DataExtractor.extract(page, selectors
                .iterator().next())));
        return extractedData;
    }
}
