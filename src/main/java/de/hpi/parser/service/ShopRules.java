package de.hpi.parser.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.EnumMap;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@ToString
public class ShopRules {

    private final EnumMap<OfferAttribute, Set<Selector>> selectors;
    private final long shopID;
}
