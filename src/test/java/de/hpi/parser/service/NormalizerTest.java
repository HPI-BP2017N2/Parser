package de.hpi.parser.service;

import org.junit.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NormalizerTest {

    @Test
    public void normalizePrice() {
        Map<OfferAttribute, String> data = new EnumMap<>(OfferAttribute.class);
        Arrays.stream(OfferAttribute.values()).forEach(offerAttribute -> data.put(offerAttribute, ""));
        data.put(OfferAttribute.PRICE, "14.99");
        Normalizer.normalizeData(data);

        assertEquals("1499", data.get(OfferAttribute.PRICE));
    }

    @Test
    public void normalizeEAN() {
        Map<OfferAttribute, String> data = new EnumMap<>(OfferAttribute.class);
        Arrays.stream(OfferAttribute.values()).forEach(offerAttribute -> data.put(offerAttribute, ""));
        data.put(OfferAttribute.EAN, "00123/07544");
        Normalizer.normalizeData(data);

        assertEquals("123, 7544", data.get(OfferAttribute.EAN));
    }
}