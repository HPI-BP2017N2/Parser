package de.hpi.parser.service;

import lombok.*;

import java.util.EnumMap;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopRules {

    private EnumMap<OfferAttribute, Set<Selector>> selectors;
    private long shopID;
}
