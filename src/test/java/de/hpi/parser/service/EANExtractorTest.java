package de.hpi.parser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EANExtractorTest {

    @Test
    public void testNoEan() {
        Document document = Jsoup.parse("abcdefg");
        Optional<String> ean = EANExtractor.extract(document);
        assertFalse(ean.isPresent());
    }

    @Test
    public void testSingleValidEAN() {
        Document document = Jsoup.parse("abcdefg4046788135653jasdlfkj");
        Optional<String> ean = EANExtractor.extract(document);
        assertTrue(ean.isPresent());
    }

    @Test
    public void testSingleInvalidEAN() {
        Document document = Jsoup.parse("abcdefg5046788135653jasdlfkj");
        Optional<String> ean = EANExtractor.extract(document);
        assertFalse(ean.isPresent());
    }

    @Test
    public void testInvalidAndValidEAN() {
        Document document = Jsoup.parse("abcdefg5046788135653jasdlfkj004046788135653");
        Optional<String> ean = EANExtractor.extract(document);
        assertTrue(ean.isPresent());
        assertEquals("4046788135653", ean.get());
    }

    @Test
    public void testTwoValidAndEqualEAN() {
        Document document = Jsoup.parse("abcdefg4046788135653jasdlfkj004046788135653");
        Optional<String> ean = EANExtractor.extract(document);
        assertTrue(ean.isPresent());
        assertEquals("4046788135653", ean.get());
    }

    @Test
    public void testTwoValidAndNotEqualEAN() {
        Document document = Jsoup.parse("abcdefg4010962102564jasdlfkj004046788135653");
        Optional<String> ean = EANExtractor.extract(document);
        assertFalse(ean.isPresent());
    }
}