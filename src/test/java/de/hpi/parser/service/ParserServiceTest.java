package de.hpi.parser.service;

import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.persistence.ParsedOffer;
import de.hpi.parser.persistence.ParsedOfferRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@RunWith(MockitoJUnitRunner.class)
public class ParserServiceTest {

    private Document pageExample;

    @Mock
    private ShopRulesGenerator shopRulesGenerator;

    @Mock
    private ParsedOfferRepository parsedOfferRepository;

    @InjectMocks
    private ParserService parserService;

    @Before
    public void setup() throws IOException {
        setPageExample(Jsoup.parse(getClass().getClassLoader().getResourceAsStream("PageExample.html"), "UTF-8", ""));
    }

    @Test
    public void successfulExtractData() {
        EnumMap<OfferAttribute, Set<Selector>> selectorMap = new EnumMap<>(OfferAttribute.class);
        Set<Selector> selectors = new HashSet<>();
        selectors.add(new TextNodeSelector("#testProduct > span:nth-child(2)"));
        selectorMap.put(OfferAttribute.EAN, selectors);
        ShopRules rules = new ShopRules(selectorMap, 1234L);
        doReturn(rules).when(getShopRulesGenerator()).getRules(anyLong());

        Answer<ParsedOffer> answer = invocationOnMock -> {
            ParsedOffer parsedOffer = invocationOnMock.getArgument(0);
            assertEquals("1234567", parsedOffer.getEan());
            return parsedOffer;
        };
        doAnswer(answer).when(getParsedOfferRepository()).save(any());
        getParserService().extractData(new CrawledPage(null, 1234L, getPageExample().html(),"google.de"));
    }


    @Test
    public void unsuccessfulExtractData() {
        EnumMap<OfferAttribute, Set<Selector>> selectorMap = new EnumMap<>(OfferAttribute.class);
        Set<Selector> selectors = new HashSet<>();
        selectors.add(new TextNodeSelector("#testProduct > span:nth-child(6)"));
        selectorMap.put(OfferAttribute.EAN, selectors);
        ShopRules rules = new ShopRules(selectorMap, 1234L);
        doReturn(rules).when(getShopRulesGenerator()).getRules(anyLong());

        Answer<ParsedOffer> answer = invocationOnMock -> {
            ParsedOffer parsedOffer = invocationOnMock.getArgument(0);
            assertNull(parsedOffer.getEan());
            return parsedOffer;
        };
        doAnswer(answer).when(getParsedOfferRepository()).save(any());
        getParserService().extractData(new CrawledPage(null, 1234L, getPageExample().html(),"google.de"));

    }
}