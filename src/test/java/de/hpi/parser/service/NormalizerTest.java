package de.hpi.parser.service;

import org.junit.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NormalizerTest {

    @Test
    public void normalizePrice() {
        assertEquals("1499", Normalizer.normalizeData("14.99", OfferAttribute.PRICE));
    }

    @Test
    public void normalizeEAN() {
        assertEquals("123, 7544", Normalizer.normalizeData("00123/07544", OfferAttribute.EAN));
    }
}