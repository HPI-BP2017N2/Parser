package de.hpi.parser.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class TextNodeSelector extends Selector {

    @JsonCreator
    TextNodeSelector(@JsonProperty(value = "cssSelector") String cssSelector) {
        super(NodeType.TEXT_NODE, cssSelector);
    }

}
