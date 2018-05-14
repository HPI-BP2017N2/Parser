package de.hpi.parser.service;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Set;


class SelectorMap extends EnumMap<OfferAttribute, Set<Selector>> {

    SelectorMap() {
        super(OfferAttribute.class);
        Arrays.stream(OfferAttribute.values()).forEach(offerAttribute -> put(offerAttribute, new LinkedHashSet<>()));
    }

}
