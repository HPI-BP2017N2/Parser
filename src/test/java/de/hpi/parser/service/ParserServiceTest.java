package de.hpi.parser.service;

import de.hpi.parser.dto.CrawledPage;
import de.hpi.parser.persistence.ParsedOffer;
import de.hpi.parser.persistence.IParsedOfferRepository;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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
    private IParsedOfferRepository parsedOfferRepository;

    @InjectMocks
    private ParserService parserService;

    @Before
    public void setup() throws IOException {
        setPageExample(Jsoup.parse(getClass().getClassLoader().getResourceAsStream("PageExample.html"), "UTF-8", ""));
    }

    @Test
    public void successfulExtractData() {
        SelectorMap selectorMap = new SelectorMap();
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
        SelectorMap selectorMap = new SelectorMap();
        Set<Selector> selectors = new HashSet<>();
        selectors.add(new TextNodeSelector("#testProduct > span:nth-child(6)"));
        selectorMap.put(OfferAttribute.EAN, selectors);
        ShopRules rules = new ShopRules(selectorMap, 1234L);
        doReturn(rules).when(getShopRulesGenerator()).getRules(anyLong());

        Answer<ParsedOffer> answer = invocationOnMock -> {
            ParsedOffer parsedOffer = invocationOnMock.getArgument(0);
            assertEquals("", parsedOffer.getEan());
            return parsedOffer;
        };
        doAnswer(answer).when(getParsedOfferRepository()).save(any());
        getParserService().extractData(new CrawledPage(null, 1234L, getPageExample().html(),"google.de"));

    }

    @Test
    public void successfulExtractDataWithScore() {
        SelectorMap selectorMap = new SelectorMap();
        TextNodeSelector selectorA = new TextNodeSelector("#testProduct > span:nth-child(2)");
        selectorA.setNormalizedScore(0.9);
        TextNodeSelector selectorB = new TextNodeSelector("#testProduct > span:nth-child(4)");
        selectorB.setNormalizedScore(0.5);
        AttributeNodeSelector selectorC = new AttributeNodeSelector("#babushka > span:nth-child(1)[itemprop]","itemprop");
        selectorC.setNormalizedScore(0.5);
        Set<Selector> selectors = new HashSet<>(Arrays.asList(selectorA, selectorB, selectorC));
        selectorMap.put(OfferAttribute.EAN, selectors);
        ShopRules rules = new ShopRules(selectorMap, 1234L);
        doReturn(rules).when(getShopRulesGenerator()).getRules(anyLong());

        Answer<ParsedOffer> answer = invocationOnMock -> {
            ParsedOffer parsedOffer = invocationOnMock.getArgument(0);
            assertEquals("890890", parsedOffer.getEan());
            return parsedOffer;
        };
        doAnswer(answer).when(getParsedOfferRepository()).save(any());
        getParserService().extractData(new CrawledPage(null, 1234L, getPageExample().html(),"google.de"));
    }

    @Test
    public void testSuccessfulDataExtraction() {
        SelectorMap selectorMap = new SelectorMap();
        Set<Selector> selectors = new HashSet<>();
        Path pathToBlock = new Path();
        pathToBlock.add(new PathID(0));
        pathToBlock.add(new PathID(0));
        selectors.add(new DataNodeSelector("head > script", pathToBlock, "$.products[1].blums"));
        selectorMap.put(OfferAttribute.EAN, selectors);
        ShopRules rules = new ShopRules(selectorMap, 1234L);
        doReturn(rules).when(getShopRulesGenerator()).getRules(anyLong());

        Answer<ParsedOffer> answer = invocationOnMock -> {
            ParsedOffer parsedOffer = invocationOnMock.getArgument(0);
            assertEquals("456", parsedOffer.getEan());
            return parsedOffer;
        };
        doAnswer(answer).when(getParsedOfferRepository()).save(any());
        getParserService().extractData(new CrawledPage(null, 1234L, getPageExample().html(),"google.de"));
    }

    @Test
    public void testUseEANGenericRulesOverSelectors() throws IOException {
        Document doc = Jsoup.parse(getClass().getClassLoader().getResourceAsStream("PageExample3.html"), "UTF-8", "");

        SelectorMap selectorMap = new SelectorMap();
        Set<Selector> selectors = new HashSet<>();
        selectors.add(new TextNodeSelector("#iDontWantToFindThis"));
        selectorMap.put(OfferAttribute.EAN, selectors);
        ShopRules rules = new ShopRules(selectorMap, 1234L);
        doReturn(rules).when(getShopRulesGenerator()).getRules(anyLong());

        Answer<ParsedOffer> answer = invocationOnMock -> {
            ParsedOffer parsedOffer = invocationOnMock.getArgument(0);
            assertEquals("8717868161157", parsedOffer.getEan());
            return parsedOffer;
        };
        doAnswer(answer).when(getParsedOfferRepository()).save(any());
        getParserService().extractData(new CrawledPage(null, 1234L, doc.html(),"google.de"));
    }



    @Test
    public void testUseSelectorsIfMultipleEan() throws IOException {
        Document doc = Jsoup.parse(getClass().getClassLoader().getResourceAsStream("PageExample2.html"), "UTF-8", "");

        SelectorMap selectorMap = new SelectorMap();
        Set<Selector> selectors = new HashSet<>();
        selectors.add(new TextNodeSelector("#iDontWantoFindThis"));
        selectorMap.put(OfferAttribute.EAN, selectors);
        ShopRules rules = new ShopRules(selectorMap, 1234L);
        doReturn(rules).when(getShopRulesGenerator()).getRules(anyLong());

        Answer<ParsedOffer> answer = invocationOnMock -> {
            ParsedOffer parsedOffer = invocationOnMock.getArgument(0);
            assertEquals("8717868149537", parsedOffer.getEan());
            return parsedOffer;
        };
        doAnswer(answer).when(getParsedOfferRepository()).save(any());
        getParserService().extractData(new CrawledPage(null, 1234L, doc.html(),"google.de"));
    }
}