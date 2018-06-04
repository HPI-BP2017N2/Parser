package de.hpi.parser.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Normalizer {

    private Normalizer() {}

    static String normalizeData(String content, OfferAttribute offerAttribute) {
        switch (offerAttribute) {
            case EAN:
                return normalizeEAN(content);
            case PRICE:
                return normalizePrice(content);
            default:
                return content;
        }
    }

    private static String normalizeEAN(String content) {
        if (content.isEmpty()) return content;
        Matcher matcher = Pattern.compile("\\d+").matcher(content);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            if (result.length() > 0) result.append(", ");
            long eanWithoutLeadingZeroes = Long.parseLong(matcher.group());
            result.append(Long.toString(eanWithoutLeadingZeroes));
        }
        return result.toString();
    }

    private static String normalizePrice(String content) {
        if (content.isEmpty()) return content;
        content = content
                .replace(".", "")
                .replace(",", "");
        return content;
    }
}
