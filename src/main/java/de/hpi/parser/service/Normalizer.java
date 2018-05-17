package de.hpi.parser.service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Normalizer {

    private Normalizer() {}

    static void normalizeData(Map<OfferAttribute, String> extractedData) {
        normalizePrice(extractedData);
        normalizeEAN(extractedData);
    }

    private static void normalizeEAN(Map<OfferAttribute, String> extractedData) {
        String normalized = extractedData.get(OfferAttribute.EAN);
        if (normalized.isEmpty()) return;
        Matcher matcher = Pattern.compile("\\d+").matcher(normalized);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            if (result.length() > 0) result.append(", ");
            long eanWithoutLeadingZeroes = Long.parseLong(matcher.group());
            result.append(Long.toString(eanWithoutLeadingZeroes));
        }
        extractedData.put(OfferAttribute.EAN, result.toString());
    }

    private static void normalizePrice(Map<OfferAttribute, String> extractedData) {
        String normalized = extractedData.get(OfferAttribute.PRICE);
        if (normalized.isEmpty()) return;
        normalized = normalized
                .replace(".", "")
                .replace(",", "");
        extractedData.put(OfferAttribute.PRICE, normalized);
    }
}
