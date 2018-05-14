package de.hpi.parser.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AttributeNodeSelector.class, name = "attributeNodeSelector"),
        @JsonSubTypes.Type(value = DataNodeSelector.class, name = "dataNodeSelector"),
        @JsonSubTypes.Type(value = TextNodeSelector.class, name = "textNodeSelector")
})
abstract class Selector {

    public enum NodeType {
        ATTRIBUTE_NODE,
        DATA_NODE,
        TEXT_NODE,
    }

    private double normalizedScore;

    private int score;

    private int leftCutIndex;

    private int rightCutIndex;

    private NodeType nodeType;

    private String cssSelector;

    @JsonCreator
    Selector(@JsonProperty(value = "nodeType") NodeType nodeType, @JsonProperty(value = "cssSelector") String
            cssSelector) {
        setNodeType(nodeType);
        setCssSelector(cssSelector);
    }
}
