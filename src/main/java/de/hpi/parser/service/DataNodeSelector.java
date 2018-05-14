package de.hpi.parser.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
class DataNodeSelector extends Selector {

    private Path pathToBlock;

    private String jsonPath;

    @JsonCreator
    DataNodeSelector(@JsonProperty(value = "cssSelector") String cssSelector, @JsonProperty(value = "pathToBlock") Path
            pathToBlock, @JsonProperty(value = "jsonPath") String jsonPath) {
        super(NodeType.DATA_NODE, cssSelector);
        setPathToBlock(pathToBlock);
        setJsonPath(jsonPath);
    }

}
