package de.hpi.parser.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.EAN;
import org.jsoup.nodes.Document;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class EANExtractor { //Singleton

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private static Validator validator;

    private EANExtractor() {}

    static {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        setValidator(vf.getValidator());
    }

    static Optional<String> extract(Document document) {
        Set<String> allDigitsMatches = new HashSet<>();
        Matcher digits = Pattern.compile("[1-9]\\d+").matcher(document.html());
        while (digits.find()) {
            allDigitsMatches.add(digits.group());
        }

        allDigitsMatches = allDigitsMatches.stream()
                .filter(digitsSequence -> getValidator().validate(new Article(digitsSequence)).isEmpty())
                .collect(Collectors.toSet());

        return (allDigitsMatches.size() == 1) ? Optional.of(allDigitsMatches.iterator().next()) : Optional.empty();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Article {
        @EAN
        private final String ean;
    }

}
