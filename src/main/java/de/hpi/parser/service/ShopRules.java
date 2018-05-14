package de.hpi.parser.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopRules {

    private SelectorMap selectorMap;
    private long shopID;
}
