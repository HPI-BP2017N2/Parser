package de.hpi.parser.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hpi.parser.exception.BlockNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@Setter(AccessLevel.PRIVATE)
@ToString
class Script {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String content;

    Script(String content) {
        allowAllObjectMapperFeatures();
        setContent(content);
    }

    private void allowAllObjectMapperFeatures() {
        Arrays.stream(JsonParser.Feature.values()).forEach(feature -> getObjectMapper().configure(feature, true));
    }

    Script getBlock(int blockIndex) {
        int currentBlockID = 0;
        int bracketCount = 0;
        int startIndex = getContent().indexOf('{');
        if (startIndex == -1) throw new BlockNotFoundException("There is no block within this script");

        for (int iChar = startIndex; iChar < getContent().length(); iChar++) {
            char c = getContent().charAt(iChar);
            if (c == '{') bracketCount++;
            else if (c == '}') bracketCount--;
            if (bracketCount == 0) {
                if (currentBlockID == blockIndex) return new Script(getContent().substring(startIndex, iChar + 1));
                startIndex = getContent().indexOf('{', iChar + 1);
                iChar = startIndex - 1;
                currentBlockID++;
            }
        }
        throw new BlockNotFoundException("Could not find block with id " + blockIndex);
    }

}
