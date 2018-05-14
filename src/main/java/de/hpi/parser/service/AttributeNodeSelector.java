package de.hpi.parser.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class AttributeNodeSelector extends Selector {

    private String attributeName;

    @JsonCreator
    AttributeNodeSelector(@JsonProperty(value = "cssSelector") String cssSelector, @JsonProperty(value =
            "attributeName") String
            attributeName){
        super(NodeType.ATTRIBUTE_NODE, cssSelector);
        setAttributeName(attributeName);
    }

}
