package de.hpi.parser.service;

import de.hpi.parser.dto.CrawledPage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter(AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@Service
public class ParserService implements IParserService {

    @Override
    public void extractData(CrawledPage crawledPage) {
        /*
        Will be implemented later
         */
    }
}
